package com.manara.backend.controller.rest;

import com.manara.backend.dto.ParentProfileResponseDto;
import com.manara.backend.dto.UpdateProfileRequest;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.User;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.ParentService;
import com.manara.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parent/profile")
public class ParentProfileController {

    @Autowired
    private ParentService parentService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Profil introuvable"));
        }
        return ResponseEntity.ok(ParentProfileResponseDto.from(profiles.getFirst()));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UpdateProfileRequest dto) {

        User user = userDetails.getUser();
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName()  != null) user.setLastName(dto.getLastName());
        if (dto.getEmail()     != null) user.setEmail(dto.getEmail());
        parentService.updateUser(user);

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.getFirst();
            if (dto.getPhoneNumber()            != null) profile.setPhoneNumber(dto.getPhoneNumber());
            if (dto.getAddressLine1()            != null) profile.setAddressLine1(dto.getAddressLine1());
            if (dto.getCity()                    != null) profile.setCity(dto.getCity());
            if (dto.getProvince()                != null) profile.setProvince(dto.getProvince());
            if (dto.getEmergencyName()           != null) profile.setEmergencyName(dto.getEmergencyName());
            if (dto.getEmergencyRelationship()   != null) profile.setEmergencyRelationship(dto.getEmergencyRelationship());
            if (dto.getEmergencyPhone()          != null) profile.setEmergencyPhone(dto.getEmergencyPhone());
            profile.setUpdatedAt(LocalDateTime.now());
            parentService.updateProfile(profile);
            return ResponseEntity.ok(ParentProfileResponseDto.from(profile));
        }

        return ResponseEntity.ok(Map.of("message", "Profil mis à jour"));
    }
}
