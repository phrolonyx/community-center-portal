package com.manara.backend.config;

import com.manara.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Configuration de Spring Security.
 * Definit les regles d'acces, la page de connexion et le cryptage des mots de passe.
 */

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactiver CSRF (simplifie les formulaires POST)
                .csrf(csrf -> csrf.disable())

                // Regles d'acces aux pages
                .authorizeHttpRequests(auth -> auth
                        // Pages publiques (pas besoin d'etre connecte)
                        .requestMatchers("/connexion", "/inscription", "/inscription/**",
                                "/images/**", "/js/**", "/css/**", "/webjars/**").permitAll()
                        // Pages admin : seulement pour le role ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // Pages parent : seulement pour le role PARENT
                        .requestMatchers("/dashboardParent/**").hasAuthority("PARENT")
                        // Toutes les autres pages sont publiques
                        .anyRequest().permitAll()
                )

                // Configuration du formulaire de connexion
                .formLogin(form -> form
                        .loginPage("/connexion")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                )

                // Configuration de la deconnexion
                .logout(logout -> logout
                        .logoutSuccessUrl("/connexion?logout")
                        .permitAll()
                )

                // Page d'erreur si l'utilisateur n'a pas les droits
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }

    // Encodeur BCrypt pour les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Fournisseur d'authentification qui utilise notre UserDetailsService
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
