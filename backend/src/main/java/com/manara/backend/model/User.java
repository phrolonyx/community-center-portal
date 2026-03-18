package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;

    @Column(nullable = false)
    @Email(message = "Le format du courriel n'est pas valide")
    @NotBlank(message = "Le courriel ne peut pas être vide")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String passwordHash;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String firstName;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String lastName;

    private String userImagePath;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private Boolean enabled;

    public User() {}

    public User(Role role, String email, String passwordHash, String firstName, String lastName, String userImagePath, Boolean enabled) {
        this.role = role;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userImagePath = userImagePath;
        createdAt = LocalDateTime.now();
        this.enabled = enabled;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
