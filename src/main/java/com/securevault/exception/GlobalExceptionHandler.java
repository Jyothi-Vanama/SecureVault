package com.securevault.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handleRuntimeException(RuntimeException ex) {

        return Map.of("message", ex.getMessage());

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
public Map<String, String> handleValidationException(
        MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
    }

    return errors;
}

}