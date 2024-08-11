package ru.zhadaev.taskmanagementsystem.errors;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CustomError {
    private final Timestamp timestamp;
    private final String status;
    private final String message;
}