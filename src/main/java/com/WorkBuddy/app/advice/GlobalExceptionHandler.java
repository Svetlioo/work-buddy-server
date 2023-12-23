package com.WorkBuddy.app.advice;

import com.WorkBuddy.app.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Global exception if something unexpected goes wrong
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // File Uploading Exceptions //

    @ExceptionHandler(CSVIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleIOException(CSVIOException e) {
        return new ResponseEntity<>("IO Exception: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // The specific invalid date is included in the exception message
    @ExceptionHandler(InvalidDateFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidDateFormatException(InvalidDateFormatException e) {
        return new ResponseEntity<>("Invalid Date Format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // If the employeeID or the projectID are not numbers
    @ExceptionHandler(CSVProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleCSVProcessingException(CSVProcessingException e) {
        return new ResponseEntity<>("CSV Processing Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // If the values are more or less than 4
    @ExceptionHandler(InvalidCSVFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleParseException(InvalidCSVFormatException e) {
        return new ResponseEntity<>("Parse Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyCSVException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleEmptyCSVException(EmptyCSVException e) {
        return new ResponseEntity<>("Empty File Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // If the file is not included in the form data
    @ExceptionHandler(MissingFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingFileException(MissingFileException e) {
        return new ResponseEntity<>("Missing File Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // If the file is not a csv file
    @ExceptionHandler(IncompatibleFileTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIncompatibleFileTypeException(IncompatibleFileTypeException e) {
        return new ResponseEntity<>("Incompatible File Type Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // A second validation before the main algorithm if after all validations are bypassed somehow
    @ExceptionHandler(NoUploadedFileException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNoUploadedFileException(NoUploadedFileException e) {
        return new ResponseEntity<>("Resource Not Found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Search exceptions in the csv file
    @ExceptionHandler(PairNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePairNotFoundException(PairNotFoundException e) {
        return new ResponseEntity<>("Pair Not Found Exception: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }


    // Employee CRUD exceptions
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException e) {
        return new ResponseEntity<>("Employee Not Found Exception: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(InvalidPathVariable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleEmployeeNotFoundException(InvalidPathVariable e) {
        return new ResponseEntity<>("Invalid Path Variable Exception: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RequestBodyMissingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleRequestBodyMissingException(RequestBodyMissingException e) {
        return new ResponseEntity<>("Request Body Missing Exception: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Adding all the errors (if the first name is more than 20 characters, if the role is not only characters, ect.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));
        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }


}
