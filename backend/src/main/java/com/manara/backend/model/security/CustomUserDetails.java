package com.manara.backend.model.security;

import com.manara.backend.model.Role;
import com.manara.backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe qui enveloppe l'entite User pour que Spring Security
 * puisse l'utiliser lors de l'authentification.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // Retourne le role de l'utilisateur (ADMIN, PARENT, ANIMATEUR)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    // Spring Security utilise le email comme "username"
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Le compte est actif seulement si un admin l'a approuve
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    // Raccourci pour afficher le nom complet dans les pages
    public String getFullName() {
        return user.getFullName();
    }

    // Permet d'acceder a l'objet User complet dans les controllers
    public User getUser() {
        return user;
    }
}
