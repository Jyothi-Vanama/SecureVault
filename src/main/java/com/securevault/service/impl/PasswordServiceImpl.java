package com.securevault.service.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.securevault.dto.PasswordGeneratorRequest;
import com.securevault.dto.PasswordGeneratorResponse;
import com.securevault.dto.PasswordStrengthRequest;
import com.securevault.dto.PasswordStrengthResponse;
import com.securevault.service.PasswordService;

@Service
public class PasswordServiceImpl implements PasswordService {

    @Override
    public PasswordStrengthResponse analyzePassword(
            PasswordStrengthRequest request) {

        String password = request.getPassword();

        PasswordStrengthResponse response = new PasswordStrengthResponse();

        int score = 0;
        List<String> feedback = new ArrayList<>();

        // Length Check
        if (password.length() >= 8) {
            score++;
        } else {
            feedback.add("Password should be at least 8 characters long.");
        }

        // Uppercase Check
        if (password.matches(".*[A-Z].*")) {
            score++;
        } else {
            feedback.add("Add at least one uppercase letter.");
        }

        // Lowercase Check
        if (password.matches(".*[a-z].*")) {
            score++;
        } else {
            feedback.add("Add at least one lowercase letter.");
        }

        // Number Check
        if (password.matches(".*\\d.*")) {
            score++;
        } else {
            feedback.add("Add at least one number.");
        }

        // Special Character Check
        if (password.matches(".*[@$!%*?&].*")) {
            score++;
        } else {
            feedback.add("Add at least one special character.");
        }

        response.setScore(score);

        if (score <= 2) {
            response.setStrength("Weak");
        } else if (score <= 4) {
            response.setStrength("Medium");
        } else {
            response.setStrength("Strong");
        }

        if (feedback.isEmpty()) {
            feedback.add("Excellent! Your password is strong.");
        }

        response.setFeedback(feedback);

        return response;
    }

    @Override
public PasswordGeneratorResponse generatePassword(
        PasswordGeneratorRequest request) {

    String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lower = "abcdefghijklmnopqrstuvwxyz";
    String numbers = "0123456789";
    String symbols = "@$!%*?&";

    String characters = "";

    if (request.isUppercase()) {
        characters += upper;
    }

    if (request.isLowercase()) {
        characters += lower;
    }

    if (request.isNumbers()) {
        characters += numbers;
    }

    if (request.isSymbols()) {
        characters += symbols;
    }

    SecureRandom random = new SecureRandom();

    StringBuilder password = new StringBuilder();

    for (int i = 0; i < request.getLength(); i++) {

        int index = random.nextInt(characters.length());

        password.append(characters.charAt(index));
    }

    return new PasswordGeneratorResponse(password.toString());

}
}