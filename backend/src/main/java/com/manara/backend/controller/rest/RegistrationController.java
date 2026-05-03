package com.manara.backend.controller.rest;

import com.manara.backend.dto.ActivityRegistrationResponseDto;
import com.manara.backend.dto.RegistrationRequest;
import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Child;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
}
