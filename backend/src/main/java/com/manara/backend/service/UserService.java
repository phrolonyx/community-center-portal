package com.manara.backend.service;

import com.manara.backend.model.Role;
import com.manara.backend.model.User;
import com.manara.backend.repository.RoleRepository;
import com.manara.backend.repository.UserRepository;
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

    public void createUser(Role role, String email, String password_hash, String first_name, String last_name, String user_image_path, Boolean enabled) {
        if (role == null) {
            role = roleRepository.findByIsDefaultTrue().orElseThrow();
        }
        userRepository.save(new User(role, email, password_hash, first_name, last_name, user_image_path, enabled));
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

    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmailAndPasswordHash(email, password).orElse(null);
        return user != null;
    }
}
