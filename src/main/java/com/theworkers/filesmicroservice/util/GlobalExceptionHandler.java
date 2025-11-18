package com.theworkers.templatemicroservice.util;

import com.theworkers.templatemicroservice.model.output.WebResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidateWebException.class)
    public ResponseEntity<WebResponse<?>> handleValidationWebException(
            ValidateWebException ex
    ) {
        return new ResponseEntity<>(
                ex.getWebResponse(),
                ex.getWebResponse().getCodeStatus()
        );
    }

    // Handle general business exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex,
            WebRequest request
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("message", ex.getMessage());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        // Check if there's a @ResponseStatus annotation on the exception
        ResponseStatus responseStatus = ex
                .getClass()
                .getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            status = responseStatus.value();
        }

        // Skip security exceptions - these are handled by Spring Security
        if (
                ex instanceof AuthenticationException ||
                        ex instanceof AccessDeniedException
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of(
                            "status",
                            "error",
                            "message",
                            "Access denied",
                            "code",
                            HttpStatus.FORBIDDEN.value()
                    )
            );
        }

        body.put("code", status.value());
        return new ResponseEntity<>(body, status);
    }

    // Handle validation exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = "";
                    if (error instanceof FieldError) {
                        fieldName = ((FieldError) error).getField();
                    } else {
                        fieldName = error.getObjectName();
                    }
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        response.put("status", "error");
        response.put("message", "Validation failed");
        response.put("errors", errors);
        response.put("code", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            errors.put(propertyPath, message);
        }

        response.put("status", "error");
        response.put("message", "Validation failed");
        response.put("errors", errors);
        response.put("code", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
