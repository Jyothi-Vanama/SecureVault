package com.securevault.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.securevault.dto.ApiResponse;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
public ApiResponse<Object> handleResourceNotFoundException(
        ResourceNotFoundException ex) {

    return new ApiResponse<>(
        false,
        ex.getMessage(),
        null
);
}
@ExceptionHandler(MethodArgumentNotValidException.class)
public ApiResponse<Map<String, String>> handleValidationException(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
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

    return new ApiResponse<>(
        false,
        ex.getMessage(),
        null
);
}

@ExceptionHandler(UserNotFoundException.class)
public ApiResponse<Object> handleUserNotFoundException(
        UserNotFoundException ex) {

    return new ApiResponse<>(
        false,
        ex.getMessage(),
        null
);
}

@ExceptionHandler(InvalidCredentialsException.class)
public ApiResponse<Object> handleInvalidCredentialsException(
        InvalidCredentialsException ex) {

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

    return new ApiResponse<>(
            false,
            ex.getMessage(),
            null
    );
}

}