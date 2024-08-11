package ru.zhadaev.taskmanagementsystem.exception;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String s) {
        super(s);
    }
}
