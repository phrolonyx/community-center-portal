package com.manara.backend.validation;

import com.manara.backend.dto.ActivitySessionRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAgeActivitySessionValidator implements ConstraintValidator<ValidAgeActivitySession, ActivitySessionRequest> {

    @Override
    public boolean isValid(ActivitySessionRequest activitySessionRequest, ConstraintValidatorContext constraintValidatorContext) {

        if (activitySessionRequest == null) {
            return true;
        }

        if (activitySessionRequest.getMinAge() == null || activitySessionRequest.getMaxAge() == null) {
            return true;
        }

        return activitySessionRequest.getMinAge() <= activitySessionRequest.getMaxAge();
    }
}
