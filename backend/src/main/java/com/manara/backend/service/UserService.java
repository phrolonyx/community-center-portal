package com.manara.backend.service;

import com.manara.backend.dto.InscriptionRequest;
import com.manara.backend.model.Role;
import com.manara.backend.model.User;
import com.manara.backend.repository.RoleRepository;
import com.manara.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Service pour gerer les utilisateurs.
 * Contient la logique d'inscription, recherche et gestion des comptes.
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Creer un utilisateur (methode existante des collegues)
    public void createUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void updateRole(Integer userId, Integer roleId) {
        User user = userRepository.findById(userId).orElseThrow();
        Role role = roleRepository.findById(roleId).orElseThrow();
        user.setRole(role);
    }

    @Transactional
    public void toggleEnabled(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setEnabled(!user.getEnabled());
    }

    // Methode existante des collegues (non utilisee dans notre partie)
    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmailAndPasswordHash(email, password).orElse(null);
        return user != null;
    }

    // Recherche d'utilisateurs (methode existante des collegues)
    public List<User> search(String keyword) {
        List<User> results = userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword
                );
        for (User user : userRepository.findAll()) {
            if (user.getRole().getName().toLowerCase().contains(keyword.toLowerCase())) {
                if (!results.contains(user)) {
                    results.add(user);
                }
            }
        }
        return results;
    }

    public long countAllUsers() {
        return userRepository.count();
    }

    public long countUsersByRole(String roleName) {
        return userRepository.countByRoleName(roleName);
    }

    public long countDisabledUsers() {
        return userRepository.countByEnabledFalse();
    }

    public List<User> findPendingUsers() {
        return userRepository.findByEnabledFalse();
    }



    // Creer un compte a partir du formulaire d'inscription
    @Transactional
    public User registerUser(InscriptionRequest dto) {
        // Trouver le role (PARENT ou ANIMATEUR)
        Role role = roleRepository.findByName(dto.getRole())
                .orElseThrow(() -> new RuntimeException("Rôle introuvable : " + dto.getRole()));

        // Creer l'utilisateur avec mot de passe crypte
        User user = new User(
                role,
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getPrenom(),
                dto.getNom(),
                null,
                false  // compte desactive par defaut (un admin doit l'approuver)
        );

        return userRepository.save(user);
    }

    // Verifier si un email existe deja dans la BD
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
