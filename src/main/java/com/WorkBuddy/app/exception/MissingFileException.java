package com.WorkBuddy.app.exception;

public class MissingFileException extends RuntimeException {
    public MissingFileException(String message) {
        super(message);
    }
}
