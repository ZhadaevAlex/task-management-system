package ru.zhadaev.taskmanagementsystem.errors;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.zhadaev.taskmanagementsystem.exception.AccessPermissionException;
import ru.zhadaev.taskmanagementsystem.exception.AlreadyExistsException;
import ru.zhadaev.taskmanagementsystem.exception.NotFoundException;

import java.sql.Timestamp;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomError> onNotFoundException(NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity
                .status(status)
                .body(new CustomError(
                        new Timestamp(System.currentTimeMillis()),
                        status.getReasonPhrase(),
                        ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<CustomError> onAlreadyExistsException(AlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity
                .status(status)
                .body(new CustomError(
                        new Timestamp(System.currentTimeMillis()),
                        status.getReasonPhrase(),
                        ex.getMessage()));
    }

    @ExceptionHandler(AccessPermissionException.class)
    public ResponseEntity<CustomError> onAccessPermissionException(AccessPermissionException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        return ResponseEntity
                .status(status)
                .body(new CustomError(
                        new Timestamp(System.currentTimeMillis()),
                        status.getReasonPhrase(),
                        ex.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CustomError> onJwtException(JwtException ex) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return ResponseEntity
                .status(status)
                .body(new CustomError(
                        new Timestamp(System.currentTimeMillis()),
                        status.getReasonPhrase(),
                        ex.getMessage()));
    }
}
