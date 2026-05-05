package com.manara.backend.controller.rest;

import com.manara.backend.dto.ActivitySessionRequest;
import com.manara.backend.dto.ActivitySessionResponseDto;
import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Location;
import com.manara.backend.model.User;
import com.manara.backend.repository.ActivityRegistrationRepository;
import com.manara.backend.service.ActivityService;
import com.manara.backend.service.ActivitySessionService;
import com.manara.backend.service.EmailService;
import com.manara.backend.service.LocationService;
import com.manara.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sessions")
public class ActivitySessionController {

    @Autowired
    private ActivitySessionService sessionService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ActivityRegistrationRepository registrationRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANIMATEUR')")
    public ResponseEntity<List<ActivitySessionResponseDto>> getAll() {
        return ResponseEntity.ok(
                sessionService.findAll().stream()
                        .map(ActivitySessionResponseDto::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ANIMATEUR')")
    public ResponseEntity<ActivitySessionResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ActivitySessionResponseDto.from(sessionService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ActivitySessionResponseDto> create(
            @Valid @RequestBody ActivitySessionRequest dto) {

        ActivitySession session = buildSession(new ActivitySession(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActivitySessionResponseDto.from(sessionService.save(session)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ActivitySessionResponseDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody ActivitySessionRequest dto) {

        ActivitySession session = buildSession(sessionService.findById(id), dto);
        return ResponseEntity.ok(ActivitySessionResponseDto.from(sessionService.save(session)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sessionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Admin annule une séance entière : marque toutes les inscriptions ANNULÉ + email à chaque parent
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> cancelSession(@PathVariable Integer id) {
        ActivitySession session = sessionService.findById(id);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");
        String dateStr = session.getDateTimeStart().format(fmt);
        String activiteName = session.getActivity().getName();

        List<ActivityRegistration> regs = registrationRepository.findByActivitySessionId(id);
        int notifies = 0;

        for (ActivityRegistration r : regs) {
            // Skipper celles deja annulees
            if (r.getCancelledAt() != null) continue;

            r.setStatus("ANNULÉ");
            r.setCancelledAt(LocalDateTime.now());
            registrationRepository.save(r);

            // Email a chaque parent inscrit
            emailService.envoyerAnnulationSeance(
                    r.getParentProfile().getUser().getEmail(),
                    r.getParentProfile().getUser().getFirstName(),
                    r.getChild().getFirstName() + " " + r.getChild().getLastName(),
                    activiteName,
                    dateStr
            );
            notifies++;
        }

        return ResponseEntity.ok(Map.of(
                "message", "Séance annulée",
                "notifies", notifies
        ));
    }

    private ActivitySession buildSession(ActivitySession session, ActivitySessionRequest dto) {
        Activity activity = activityService.findById(dto.getActivityId());
        User host = userService.findById(dto.getHostId());
        Location location = locationService.findById(dto.getLocationId());

        session.setActivity(activity);
        session.setHost(host);
        session.setLocation(location);
        session.setCapacity(dto.getCapacity());
        session.setDateTimeStart(dto.getDateTimeStart());
        session.setDateTimeEnd((dto.getDateTimeEnd()));
         session.setMinAge(dto.getMinAge());
         session.setMaxAge(dto.getMaxAge());
        return session;
    }
}
