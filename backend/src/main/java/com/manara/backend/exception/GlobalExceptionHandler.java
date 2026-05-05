package com.manara.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Intercepte les erreurs @Valid — retourne le premier message de validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        // Récupérer le premier message d'erreur de validation
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> traduireErreur(err.getField(), err.getDefaultMessage()))
                .orElse("Erreur de validation.");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    // Traduit les messages techniques en messages compréhensibles
    private String traduireErreur(String field, String message) {
        return switch (field) {
            case "dateOfBirth" -> "La date de naissance est invalide — l'enfant doit être âgé de moins de 18 ans.";
            case "firstName"   -> "Le prénom est obligatoire (minimum 2 caractères).";
            case "lastName"    -> "Le nom est obligatoire (minimum 2 caractères).";
            case "email"       -> "L'adresse courriel est invalide.";
            case "password"    -> "Le mot de passe doit contenir au moins 8 caractères.";
            case "role"        -> "Le rôle est obligatoire.";
            default            -> message;
        };
    }
}
