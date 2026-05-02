package com.manara.backend.controller.rest;

import com.manara.backend.dto.AdminDashboardDto;
import com.manara.backend.dto.UserResponseDto;
import com.manara.backend.model.User;
import com.manara.backend.service.ActivitySessionService;
import com.manara.backend.service.ChildService;
import com.manara.backend.service.LocationService;
import com.manara.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> dashboard() {
        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setTotalUsers(userService.countAllUsers());
        dto.setTotalParents(userService.countUsersByRole("PARENT"));
        dto.setTotalAnimateurs(userService.countUsersByRole("ANIMATEUR"));
        dto.setTotalDisabled(userService.countDisabledUsers());
        dto.setPendingUsers(
                userService.findPendingUsers().stream()
                        .map(UserResponseDto::from)
                        .toList()
        );
        return ResponseEntity.ok(dto);
    }
}

