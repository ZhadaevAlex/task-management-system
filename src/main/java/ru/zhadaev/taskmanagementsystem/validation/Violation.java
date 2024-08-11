package ru.zhadaev.taskmanagementsystem.validation;

import lombok.Data;

@Data
public class Violation {
    private final String message;
    private final String propertyPath;
    private final Object invalidValue;
}
