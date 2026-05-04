package com.manara.backend.controller.rest;

import com.manara.backend.dto.UserResponseDto;
import com.manara.backend.model.User;
import com.manara.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getUsers(
            @RequestParam(required = false) String search) {

        List<User> users = (search == null || search.isBlank())
                ? userService.findAll()
                : userService.search(search.trim());

        return ResponseEntity.ok(
                users.stream().map(UserResponseDto::from).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(UserResponseDto.from(userService.findById(id)));
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<?> enableUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        user.setEnabled(true);
        userService.save(user);
        return ResponseEntity.ok(Map.of("message", "Compte activé"));
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<?> disableUser(@PathVariable Integer id) {
        User user = userService.findById(id);

        if ("ADMIN".equalsIgnoreCase(user.getRole().getName())) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Impossible de désactiver un administrateur"));
        }

        user.setEnabled(false);
        userService.save(user);
        return ResponseEntity.ok(Map.of("message", "Compte désactivé"));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        user.setPending(false);
        user.setEnabled(true);
        userService.save(user);
        return ResponseEntity.ok(Map.of("message", "Compte approuvé"));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> rejectUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        user.setPending(false);
        user.setEnabled(false);
        userService.save(user);
        return ResponseEntity.ok(Map.of("message", "Compte rejeté"));
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponseDto>> getUsersByRole(@PathVariable String role) {

        List<User> users = userService.findByRoleName(role);

        return ResponseEntity.ok(
                users.stream().map(UserResponseDto::from).toList()
        );
    }
}
