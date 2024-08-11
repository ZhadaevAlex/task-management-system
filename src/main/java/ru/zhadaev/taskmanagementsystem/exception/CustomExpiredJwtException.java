package ru.zhadaev.taskmanagementsystem.exception;

public class CustomExpiredJwtException extends RuntimeException {
    public CustomExpiredJwtException(String s) {
        super(s);
    }
}