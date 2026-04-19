package com.manara.backend.config;

import com.manara.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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

                // Appel de la configuration CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Regles d'acces aux pages
                .authorizeHttpRequests(auth -> auth

                        /* // Cette version n'est plus utile dans la version REST
                        // Pages publiques (pas besoin d'etre connecte)
                        .requestMatchers("/connexion", "/inscription", "/inscription/**",
                                "/images/**", "/js/**", "/css/**", "/webjars/**", "/uploads/**").permitAll()
                        // Pages admin : seulement pour le role ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        // Pages parent : seulement pour le role PARENT
                        .requestMatchers("/dashboardParent/**").hasAuthority("PARENT")
                        // Toutes les autres pages sont publiques
                        .anyRequest().permitAll() */

                        // Routes publiques
                        .requestMatchers("/auth/login", "/auth/register",
                                "/auth/check-email", "/activities",
                                "/uploads/**").permitAll()
                        // Routes admin
                        .requestMatchers("/admin/**", "/users/**",
                                "/sessions/**").hasAuthority("ADMIN")
                        // Routes parent
                        .requestMatchers("/registrations/**").hasAnyAuthority("PARENT", "ADMIN")
                        // Sécurisation par défaut et de possibles endpoints futurs
                        .anyRequest().authenticated()

                )

                // Configuration du formulaire de connexion
                // N'est plus utile en REST
                /*.formLogin(form -> form
                        .loginPage("/connexion")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                )*/

                // Configuration de la deconnexion
                // N'est plus utile en REST
                /*.logout(logout -> logout
                        .logoutSuccessUrl("/connexion?logout")
                        .permitAll()
                )*/

                // Page d'erreur si l'utilisateur n'a pas les droits
                .exceptionHandling(exception -> exception

                        // Plus nécessaire en REST
                        /*.accessDeniedPage("/access-denied")*/

                        // RÉponses d'erreur en JSON
                        .authenticationEntryPoint((req, res, ex) -> {
                            res.setStatus(401);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"Non authentifié\"}");
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            res.setStatus(403);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"error\":\"Accès refusé\"}");
                        })
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
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
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
    // Exposer le AuthenticationManager pour l'utiliser dans le controller REST
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
