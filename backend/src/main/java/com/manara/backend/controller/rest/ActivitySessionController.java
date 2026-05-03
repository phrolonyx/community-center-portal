package com.manara.backend.controller.rest;

import com.manara.backend.dto.ActivitySessionRequest;
import com.manara.backend.dto.ActivitySessionResponseDto;
import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Location;
import com.manara.backend.model.User;
import com.manara.backend.service.ActivityService;
import com.manara.backend.service.ActivitySessionService;
import com.manara.backend.service.LocationService;
import com.manara.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
@PreAuthorize("hasAuthority('ADMIN')")
public class ActivitySessionController {

    @Autowired
    private ActivitySessionService sessionService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<ActivitySessionResponseDto>> getAll() {
        return ResponseEntity.ok(
                sessionService.findAll().stream()
                        .map(ActivitySessionResponseDto::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivitySessionResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ActivitySessionResponseDto.from(sessionService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ActivitySessionResponseDto> create(
            @Valid @RequestBody ActivitySessionRequest dto) {

        ActivitySession session = buildSession(new ActivitySession(), dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActivitySessionResponseDto.from(sessionService.save(session)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivitySessionResponseDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody ActivitySessionRequest dto) {

        ActivitySession session = buildSession(sessionService.findById(id), dto);
        return ResponseEntity.ok(ActivitySessionResponseDto.from(sessionService.save(session)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sessionService.deleteById(id);
        return ResponseEntity.noContent().build();
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
        return session;
    }
}
