package com.manara.backend.controller.rest;

import com.manara.backend.dto.ActivityRequest;
import com.manara.backend.dto.ActivityResponseDto;
import com.manara.backend.model.Activity;
import com.manara.backend.service.ActivityService;
import com.manara.backend.service.ImageStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ImageStorageService imageStorageService;

    @Value("${app.upload-dir}")
    private String uploadDir;

    @GetMapping
    public ResponseEntity<List<ActivityResponseDto>> getAll() {
        return ResponseEntity.ok(
                activityService.findAll().stream()
                        .map(ActivityResponseDto::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ActivityResponseDto.from(activityService.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ActivityResponseDto> create(@Valid @RequestBody ActivityRequest dto) {
        Activity activity = new Activity();
        activity.setName(dto.getName());
        activity.setDescription(dto.getDescription());
        Activity saved = activityService.save(activity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ActivityResponseDto.from(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ActivityResponseDto> update(@PathVariable Integer id,
                                                      @Valid @RequestBody ActivityRequest dto) {
        Activity activity = activityService.findById(id);
        activity.setName(dto.getName());
        activity.setDescription(dto.getDescription());
        return ResponseEntity.ok(ActivityResponseDto.from(activityService.save(activity)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        activityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> uploadImage(@PathVariable Integer id,
                                         @RequestParam("imageFile") MultipartFile file) {
        try {
            String fileName = imageStorageService.store(file, uploadDir);
            Activity activity = activityService.findById(id);
            activity.setActivityImagePath(fileName);
            activityService.save(activity);
            return ResponseEntity.ok(Map.of("imagePath", fileName));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Échec de l'upload : " + e.getMessage()));
        }
    }
}
