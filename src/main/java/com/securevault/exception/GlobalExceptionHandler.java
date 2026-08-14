package com.securevault.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.securevault.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        logger.error("Resource not found: {}", ex.getMessage(), ex);

        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException ex) {

        logger.error("Validation failed: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(
                    error.getField(),
                    error.getDefaultMessage()
            );
        }

        return new ApiResponse<>(
                false,
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ApiResponse<Object> handleDuplicateEmailException(
            DuplicateEmailException ex) {

        logger.error("Duplicate email registration attempt: {}",
                ex.getMessage(), ex);

        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ApiResponse<Object> handleUserNotFoundException(
            UserNotFoundException ex) {

        logger.error("User not found: {}", ex.getMessage(), ex);

        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ApiResponse<Object> handleInvalidCredentialsException(
            InvalidCredentialsException ex) {

        logger.error("Invalid credentials: {}", ex.getMessage(), ex);

        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }

    @ExceptionHandler(PasswordReuseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Object> handlePasswordReuseException(
            PasswordReuseException ex) {

        logger.error("Password reuse detected: {}",
                ex.getMessage(), ex);

        return new ApiResponse<>(
                false,
                ex.getMessage(),
                null
        );
    }
}