package com.manara.backend.controller.rest;

import com.manara.backend.dto.ActivityRegistrationResponseDto;
import com.manara.backend.dto.RegistrationRequest;
import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Child;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.repository.ActivityRegistrationRepository;
import com.manara.backend.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivitySessionService sessionService;

    @Autowired
    private ChildService childService;

    @Autowired
    private ParentService parentService;

    @Autowired
    private ActivityRegistrationRepository registrationRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public ResponseEntity<List<ActivityRegistrationResponseDto>> getMyRegistrations(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) return ResponseEntity.ok(List.of());

        return ResponseEntity.ok(
                activityService.findRegistrationsByParentId(profiles.get(0).getId())
                        .stream()
                        .map(ActivityRegistrationResponseDto::from)
                        .toList()
        );
    }

    @PostMapping
    public ResponseEntity<ActivityRegistrationResponseDto> register(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody RegistrationRequest dto) {

        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ParentProfile profile = profiles.get(0);

        Child child = childService.findById(dto.getChildId());
        ActivitySession session = sessionService.findById(dto.getActivitySessionId());

        // Verification : l'enfant doit appartenir au parent connecte
        List<Child> mesEnfants = childService.findByParentProfileId(profile.getId());
        boolean estMonEnfant = mesEnfants.stream().anyMatch(c -> c.getId().equals(child.getId()));
        if (!estMonEnfant) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Verification anti-doublon : enfant pas deja inscrit a cette seance
        if (activityService.isAlreadyRegistered(dto.getChildId(), dto.getActivitySessionId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Verification de l'age : l'enfant doit etre dans l'intervalle exige par la seance
        Integer ageEnfant = child.getAge();
        if (session.getMinAge() != null && ageEnfant < session.getMinAge()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (session.getMaxAge() != null && ageEnfant > session.getMaxAge()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        ActivityRegistration registration = new ActivityRegistration();
        registration.setParentProfile(profile);
        registration.setChild(child);
        registration.setActivitySession(session);
        registration.setStatus("EN_ATTENTE");
        registration.setCreatedAt(LocalDateTime.now());

        activityService.saveRegistration(registration);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActivityRegistrationResponseDto.from(registration));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancel(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Profil parent introuvable"));
        }

        activityService.cancelRegistration(id);
        return ResponseEntity.ok(Map.of("message", "Inscription annulée"));
    }

    // Admin récupère toutes les inscriptions en attente (statut EN_ATTENTE)
    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ActivityRegistrationResponseDto>> getPending() {
        List<ActivityRegistrationResponseDto> dtos = registrationRepository.findAll().stream()
                .filter(r -> "EN_ATTENTE".equals(r.getStatus()))
                .map(ActivityRegistrationResponseDto::from)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Admin annule une inscription (avec motif optionnel envoyé dans le body)
    @PutMapping("/{id}/admin-cancel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> annulerAdmin(
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, String> body) {

        ActivityRegistration reg = registrationRepository.findById(id).orElseThrow();
        reg.setStatus("ANNULÉ");
        reg.setCancelledAt(LocalDateTime.now());
        registrationRepository.save(reg);

        String motif = (body != null) ? body.get("motif") : null;

        // Email d'annulation au parent (avec motif si fourni)
        emailService.envoyerAnnulation(
                reg.getParentProfile().getUser().getEmail(),
                reg.getParentProfile().getUser().getFirstName(),
                reg.getChild().getFirstName() + " " + reg.getChild().getLastName(),
                reg.getActivitySession().getActivity().getName(),
                motif
        );

        return ResponseEntity.ok(Map.of("message", "Inscription annulée + email envoyé"));
    }

    // Admin confirme une inscription en attente : statut -> CONFIRMÉ + email au parent
    @PutMapping("/{id}/admin-confirm")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> confirmerAdmin(@PathVariable Integer id) {
        ActivityRegistration reg = registrationRepository.findById(id)
                .orElseThrow();

        // Bloquer la confirmation si la séance est déjà pleine
        Integer sessionId = reg.getActivitySession().getId();
        Integer capacity = reg.getActivitySession().getCapacity();
        long confirmes = registrationRepository.countByActivitySessionIdAndStatus(sessionId, "CONFIRMÉ");
        if (capacity != null && confirmes >= capacity) {
            return ResponseEntity.status(409).body(Map.of(
                    "error", "Capacité maximale atteinte (" + capacity + " enfants)"
            ));
        }

        reg.setStatus("CONFIRMÉ");
        reg.setConfirmedAt(LocalDateTime.now());
        registrationRepository.save(reg);

        // Email de confirmation au parent (avec lieu + nom de l'animateur)
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");
        ActivitySession seance = reg.getActivitySession();
        String lieu = (seance.getLocation() != null) ? seance.getLocation().getName() : null;
        String nomAnimateur = (seance.getHost() != null)
                ? seance.getHost().getFirstName() + " " + seance.getHost().getLastName() : null;

        emailService.envoyerConfirmationAdmin(
                reg.getParentProfile().getUser().getEmail(),
                reg.getParentProfile().getUser().getFirstName(),
                reg.getChild().getFirstName() + " " + reg.getChild().getLastName(),
                seance.getActivity().getName(),
                seance.getDateTimeStart().format(fmt),
                lieu,
                nomAnimateur
        );

        return ResponseEntity.ok(Map.of("message", "Inscription confirmée + email envoyé"));
    }
}
