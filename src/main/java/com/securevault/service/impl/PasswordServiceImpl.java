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

    SecureRandom random = new SecureRandom();

    List<Character> passwordCharacters = new ArrayList<>();
    StringBuilder characters = new StringBuilder();

    // Add at least one character from every selected type
    if (request.isUppercase()) {
        passwordCharacters.add(
                upper.charAt(random.nextInt(upper.length()))
        );
        characters.append(upper);
    }

    if (request.isLowercase()) {
        passwordCharacters.add(
                lower.charAt(random.nextInt(lower.length()))
        );
        characters.append(lower);
    }

    if (request.isNumbers()) {
        passwordCharacters.add(
                numbers.charAt(random.nextInt(numbers.length()))
        );
        characters.append(numbers);
    }

    if (request.isSymbols()) {
        passwordCharacters.add(
                symbols.charAt(random.nextInt(symbols.length()))
        );
        characters.append(symbols);
    }

    // Make sure at least one character type is selected
    if (characters.length() == 0) {
        throw new IllegalArgumentException(
                "At least one character type must be selected."
        );
    }

    // Fill the remaining password length randomly
    while (passwordCharacters.size() < request.getLength()) {

        int index = random.nextInt(characters.length());

        passwordCharacters.add(
                characters.charAt(index)
        );
    }

    // Shuffle the characters so the guaranteed characters
    // are not always placed at the beginning
    for (int i = passwordCharacters.size() - 1; i > 0; i--) {

        int j = random.nextInt(i + 1);

        Character temp = passwordCharacters.get(i);
        passwordCharacters.set(i, passwordCharacters.get(j));
        passwordCharacters.set(j, temp);
    }

    StringBuilder password = new StringBuilder();

    for (Character character : passwordCharacters) {
        password.append(character);
    }

    return new PasswordGeneratorResponse(password.toString());
}
}