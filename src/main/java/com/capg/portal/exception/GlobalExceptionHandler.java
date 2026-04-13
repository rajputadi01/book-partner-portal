package com.capg.portal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Catch Custom "Not Found" or Business Logic Errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Runtime Exception");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 2. Catch EVERYTHING else (Database crashes, NullPointers, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalExceptions(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "System Error");
        body.put("message", "An unexpected error occurred. Please check logs.");

        // Print to console so you can debug in STS/Eclipse
        ex.printStackTrace(); 

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}