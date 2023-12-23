package com.WorkBuddy.app.exception;

public class EmptyCSVException extends RuntimeException {
    public EmptyCSVException(String message) {
        super(message);
    }
}