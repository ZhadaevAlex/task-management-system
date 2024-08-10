package ru.zhadaev.taskmanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.EnumUtils;
import ru.zhadaev.taskmanagementsystem.dao.entity.Status;

public class AllowValuesStatusValidationConstraint implements ConstraintValidator<AllowValuesStatus, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return true;
        }

        return EnumUtils.isValidEnum(Status.class, str);
    }
}
