package com.manara.backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ValidAgeActivitySessionValidator.class)
public @interface ValidAgeActivitySession {

    String message() default "Le minAge doit etre inferieur ou egale a maxAge";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
