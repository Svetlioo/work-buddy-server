package com.WorkBuddy.app.exception;

public class RequestBodyMissingException extends RuntimeException {
    public RequestBodyMissingException(String message) {
        super(message);
    }
}

