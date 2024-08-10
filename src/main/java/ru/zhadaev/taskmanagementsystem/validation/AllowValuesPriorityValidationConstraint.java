package ru.zhadaev.taskmanagementsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.EnumUtils;
import ru.zhadaev.taskmanagementsystem.dao.entity.Priority;

public class AllowValuesPriorityValidationConstraint implements ConstraintValidator<AllowValuesPriority, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        if (str == null) {
            return true;
        }

        return EnumUtils.isValidEnum(Priority.class, str);
    }
}
