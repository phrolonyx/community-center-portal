package com.manara.backend.service;

import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manara.backend.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws
            UsernameNotFoundException {
        // Utilisateur utilisateur = repo.getUtilisateurByEmail(email);
        User user = repo.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Aucun utilisateur trouvé avec l'email : " + email);
        }
        return new CustomUserDetails(user);
    }
}
