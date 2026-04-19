package com.manara.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// Détermine que cette annotation ne peut être utilisée que sur des attributs ("FIELD")
@Target({ FIELD })
// Rend l'annotation disponible lorsque l'application roule
@Retention(RUNTIME)
// Fait le pont entre cet interface et la classe qui l'instantie
@Constraint(validatedBy = IsChildValidator.class)
// Le @ devant "interface" indique qu'il s'agit d'une définition d'annotation
public @interface IsChild {
    // Message d'échec de validation par défaut
    String message() default "L'enfant a 18 ans ou plus";
    // Permettrait de regrouper plusieurs validations ensemble en fonction du contexte (mais pas dans le nôtre).
    // DOit tout de même être déclaré
    Class<?>[] groups() default {};
    // Nécessaire au cas où il y aurait des métadonnées à prendre en considération par le validateur.
    // Doit être déclaré mais laissé par défaut pour notre cas
    Class<? extends Payload>[] payload() default {};
}
