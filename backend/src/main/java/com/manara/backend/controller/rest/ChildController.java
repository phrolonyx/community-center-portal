package com.manara.backend.controller.rest;

import com.manara.backend.dto.ChildRequest;
import com.manara.backend.dto.ChildResponseDto;
import com.manara.backend.model.Child;
import com.manara.backend.model.ChildMedical;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.ChildService;
import com.manara.backend.service.ParentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    @Autowired
    private ParentService parentService;

    @GetMapping
    public ResponseEntity<List<ChildResponseDto>> getChildren(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String search) {

        List<Child> children;
        String role = userDetails.getUser().getRole().getName();

        if ("ADMIN".equals(role) || "ANIMATEUR".equals(role)) {
            children = (search == null || search.isBlank())
                    ? childService.findAll()
                    : childService.searchByName(search.trim());
        } else {
            List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
            if (profiles.isEmpty()) return ResponseEntity.ok(List.of());
            children = childService.findByParentProfileId(profiles.get(0).getId());
        }

        return ResponseEntity.ok(
                children.stream().map(ChildResponseDto::from).toList()
        );
    }

    @PostMapping
    public ResponseEntity<ChildResponseDto> createChild(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChildRequest dto) {

        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        ParentProfile profile = profiles.get(0);

        Child child = new Child();
        child.setFirstName(dto.getFirstName());
        child.setLastName(dto.getLastName());
        child.setDateOfBirth(dto.getDateOfBirth());
        child.setUpdatedAt(LocalDateTime.now());

        childService.createChild(child, profile.getId(), dto.getRelationship());

        if (dto.getMedical() != null && !dto.getMedical().isBlank()) {
            ChildMedical condition = new ChildMedical();
            condition.setCondition(dto.getMedical().trim());
            condition.setSeverity("Non spécifiée");
            condition.setUpdatedAt(LocalDateTime.now());
            childService.addMedicalCondition(condition, child.getId());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChildResponseDto.from(childService.findById(child.getId())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChildResponseDto> updateChild(
            @PathVariable Integer id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChildRequest dto) {

        Child child = childService.findById(id);
        child.setFirstName(dto.getFirstName());
        child.setLastName(dto.getLastName());
        child.setDateOfBirth(dto.getDateOfBirth());
        child.setUpdatedAt(LocalDateTime.now());
        childService.updateChild(child);

        // Mise a jour de la relation Parent-Enfant
        List<ParentProfile> profilsParent = parentService.findByUserId(userDetails.getUser().getId());
        if (!profilsParent.isEmpty()) {
            childService.updateRelationship(id, profilsParent.get(0).getId(), dto.getRelationship());
        }

        if (dto.getMedical() != null && !dto.getMedical().isBlank()) {
            List<ChildMedical> conditions = child.getMedicalConditions();
            if (conditions != null && !conditions.isEmpty()) {
                ChildMedical existing = conditions.get(0);
                existing.setCondition(dto.getMedical().trim());
                existing.setUpdatedAt(LocalDateTime.now());
                childService.updateMedicalCondition(existing);
            } else {
                ChildMedical condition = new ChildMedical();
                condition.setCondition(dto.getMedical().trim());
                condition.setSeverity("Non spécifiée");
                condition.setUpdatedAt(LocalDateTime.now());
                childService.addMedicalCondition(condition, id);
            }
        }

        return ResponseEntity.ok(ChildResponseDto.from(childService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Integer id) {
        childService.deleteChild(id);
        return ResponseEntity.noContent().build();
    }
}
