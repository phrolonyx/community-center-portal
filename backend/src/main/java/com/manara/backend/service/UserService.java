package com.manara.backend.service;

import com.manara.backend.model.Role;
import com.manara.backend.model.User;
import com.manara.backend.repository.RoleRepository;
import com.manara.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(User user) {
        /*if (user.getRole() == null) {
            user.setRole(roleRepository.findByIsDefaultTrue().orElseThrow());
        } else {
            Role role = roleRepository.findById(user.getRole().getId()).orElseThrow();
            user.setRole(role);
        }*/
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        userRepository.save(user);
    }

    // updateUser BCrypt:
    // if (!passwordEncoder.matches(updatedUser.getPasswordHash(), existing.getPasswordHash())) {
    //            existing.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
    //        }

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

    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmailAndPasswordHash(email, password).orElse(null);
        return user != null;
    }

    public List<User> search(String keyword) {

        // Recherche prenoms et noms et email
        List<User> results = userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword
                );

        // On ajoute les utilisateurs dont le role correspond au mot recherche
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

    public List<User> findPendingUsers() {  // les users a approuver dans le dashboard
        return userRepository.findByEnabledFalse();
    }





}
