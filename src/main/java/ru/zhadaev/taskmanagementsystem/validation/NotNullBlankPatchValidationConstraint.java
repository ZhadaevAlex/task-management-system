package ru.zhadaev.taskmanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullBlankPatchValidationConstraint implements ConstraintValidator<NotNullBlankPath, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return true;
        }

        return str.trim().length() > 0;
    }
}
