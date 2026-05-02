package com.manara.backend.controller.REST;

import com.manara.backend.dto.ActivityRegistrationResponseDto;
import com.manara.backend.dto.RegistrationRequest;
import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Child;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.ActivityRegistrationService;
import com.manara.backend.service.ActivitySessionService;
import com.manara.backend.service.ChildService;
import com.manara.backend.service.ParentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/registrations")
public class RegistrationController {

    @Autowired
    private ActivityRegistrationService registrationService;

    @Autowired
    private ParentService parentService;

    @Autowired
    private ChildService childService;

    @Autowired
    private ActivitySessionService activitySessionService;

    @GetMapping
    public List<ActivityRegistrationResponseDto> getMyRegistrations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ParentProfile profile = getParentProfile(userDetails);
        return registrationService.findByParentProfileId(profile.getId())
                .stream()
                .map(ActivityRegistrationResponseDto::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ActivityRegistrationResponseDto> createRegistration(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid RegistrationRequest request) {

        ParentProfile profile = getParentProfile(userDetails);
        Child child = childService.findById(request.getChildId());

        List<Child> enfants = childService.findByParentProfileId(profile.getId());
        if (enfants.stream().noneMatch(c -> c.getId().equals(child.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cet enfant n'appartient pas au parent connecte");
        }

        if (registrationService.existsActiveRegistration(request.getChildId(), request.getActivitySessionId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet enfant est deja inscrit a cette seance");
        }

        ActivitySession session = activitySessionService.findById(request.getActivitySessionId());

        ActivityRegistration registration = new ActivityRegistration();
        registration.setParentProfile(profile);
        registration.setChild(child);
        registration.setActivitySession(session);
        registration.setStatus("PENDING");
        registration.setCreatedAt(LocalDateTime.now());

        ActivityRegistration saved = registrationService.save(registration);
        return ResponseEntity.status(HttpStatus.CREATED).body(ActivityRegistrationResponseDto.from(saved));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ActivityRegistrationResponseDto> cancelRegistration(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer id) {

        ParentProfile profile = getParentProfile(userDetails);
        ActivityRegistration registration = registrationService.findById(id);

        if (!registration.getParentProfile().getId().equals(profile.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Vous ne pouvez annuler que vos propres inscriptions");
        }

        registration.setStatus("CANCELLED");
        registration.setCancelledAt(LocalDateTime.now());
        ActivityRegistration saved = registrationService.save(registration);

        return ResponseEntity.ok(ActivityRegistrationResponseDto.from(saved));
    }

    private ParentProfile getParentProfile(CustomUserDetails userDetails) {
        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profil parent introuvable");
        }
        return profiles.get(0);
    }
}
