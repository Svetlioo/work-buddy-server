package com.WorkBuddy.app.exception;

public class InvalidCSVFormatException extends RuntimeException {
    public InvalidCSVFormatException(String message) {
        super(message);
    }
}