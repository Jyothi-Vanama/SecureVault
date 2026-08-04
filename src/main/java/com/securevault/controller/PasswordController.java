package com.securevault.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securevault.dto.ApiResponse;
import com.securevault.dto.PasswordGeneratorRequest;
import com.securevault.dto.PasswordGeneratorResponse;
import com.securevault.dto.PasswordStrengthRequest;
import com.securevault.dto.PasswordStrengthResponse;
import com.securevault.service.PasswordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/strength")
    public ApiResponse<PasswordStrengthResponse> analyzePassword(
            @Valid @RequestBody PasswordStrengthRequest request) {

        PasswordStrengthResponse response =
                passwordService.analyzePassword(request);

        return new ApiResponse<>(
                true,
                "Password analyzed successfully",
                response
        );
    }

    @PostMapping("/generate")
public ApiResponse<PasswordGeneratorResponse> generatePassword(
        @Valid @RequestBody PasswordGeneratorRequest request) {

    PasswordGeneratorResponse response =
            passwordService.generatePassword(request);

    return new ApiResponse<>(
            true,
            "Password generated successfully",
            response
    );
}
}