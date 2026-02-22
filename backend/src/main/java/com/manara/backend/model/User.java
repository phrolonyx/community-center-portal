package com.manara.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password_hash;

    @Column(length = 100, nullable = false)
    private String first_name;

    @Column(length = 100, nullable = false)
    private String last_name;

    private String user_image_path;

    @Column(nullable = false)
    private LocalDate created_at;

    private LocalDate last_login;

    @Column(nullable = false)
    private Boolean enabled;

    public User() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return password_hash;
    }

    public void setPasswordHash(String password_hash) {
        this.password_hash = password_hash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getUserImagePath() {
        return user_image_path;
    }

    public void setUserImagePath(String user_image_path) {
        this.user_image_path = user_image_path;
    }

    public LocalDate getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getLastLogin() {
        return last_login;
    }

    public void setLastLogin(LocalDate last_login) {
        this.last_login = last_login;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return first_name + " " + last_name;
    }
}
