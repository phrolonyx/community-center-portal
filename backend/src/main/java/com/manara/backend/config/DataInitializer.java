package com.manara.backend.config;

import com.manara.backend.repository.RoleRepository;
import com.manara.backend.repository.UserRepository;
import com.manara.backend.model.Role;
import com.manara.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("PARENT", "Parent ou tuteur qui peut inscrire ses enfants", true));
            roleRepository.save(new Role("ADMIN", "Administrateur qui peut valider les inscriptions et créer les activités", false));
            roleRepository.save(new Role("ANIMATEUR", "Animateur de séances d'activités", false));
        }
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            User admin = new User();
            admin.setEmail("admin@admin.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("Manara");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setRole(adminRole);
            admin.setEnabled(true);
            userRepository.save(admin);
        }
    }
}
