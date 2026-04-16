package com.capg.portal.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler 
{
    // Helper method to determine if the request came from our Thymeleaf UI
    private boolean isWebUiRequest(HttpServletRequest request) 
    {
        return request.getRequestURI().startsWith("/web/") || request.getRequestURI().equals("/");
    }

    // Helper method to build the HTML Error Page
    private ModelAndView buildErrorPage(HttpStatus status, String error, String message, HttpServletRequest request) 
    {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("status", status.value());
        mav.addObject("error", error);
        mav.addObject("message", message);
        mav.addObject("path", request.getRequestURI());
        mav.addObject("timestamp", LocalDateTime.now());
        return mav;
    }

    // Helper method to build the JSON REST Response
    private ResponseEntity<ErrorResponse> buildJsonResponse(HttpStatus status, String error, String message, HttpServletRequest request) 
    {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), error, message, request.getRequestURI());
        return new ResponseEntity<>(errorResponse, status);
    }

    // 1. Handle 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) 
    {
        if (isWebUiRequest(request)) return buildErrorPage(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage(), request);
        return buildJsonResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), request);
    }

    // 2. Handle 409 - Resource Already Exists
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public Object handleResourceAlreadyExists(ResourceAlreadyExistsException ex, HttpServletRequest request) 
    {
        if (isWebUiRequest(request)) return buildErrorPage(HttpStatus.CONFLICT, "Data Conflict", ex.getMessage(), request);
        return buildJsonResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage(), request);
    }

    // 3. Handle 400 - Business Logic Validation (Like the 100% Royalty Rule)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) 
    {
        if (isWebUiRequest(request)) return buildErrorPage(HttpStatus.BAD_REQUEST, "Invalid Request", ex.getMessage(), request);
        return buildJsonResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
    }

    // 4. Handle 400 - Validation Errors (@Valid annotations in REST payload)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) 
    {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        if (isWebUiRequest(request)) return buildErrorPage(HttpStatus.BAD_REQUEST, "Validation Failed", errorMessage, request);
        return buildJsonResponse(HttpStatus.BAD_REQUEST, "Validation Failed", errorMessage, request);
    }

    // 5. Handle 500 - Global Fallback (Unexpected errors)
    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception ex, HttpServletRequest request) 
    {
        ex.printStackTrace(); // Keep for server logs
        
        String msg = "An unexpected error occurred. Please try again or contact support.";
        if (isWebUiRequest(request)) return buildErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "System Error", msg, request);
        return buildJsonResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", msg, request);
    }
}