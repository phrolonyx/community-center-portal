package com.manara.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

// Implémentation de l'annotation vi ConstraintValidator
public class IsChildValidator implements ConstraintValidator<IsChild, LocalDateTime> {
    // Age maximal: si l'enfant est majeur il ne peut être inscrit
    private static final int MAX_AGE = 18;
    @Override
    public boolean isValid(LocalDateTime birthDate, ConstraintValidatorContext context) {
        // Afin de ne pas empiéter sur @NotNull on retourne true en cas de valeur null:
        if (birthDate == null) {
            return true;
        }
        LocalDateTime childhoodLimit = LocalDateTime.now().minusYears(MAX_AGE);
        return birthDate.isAfter(childhoodLimit);
    }
}
