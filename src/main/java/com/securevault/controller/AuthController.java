package com.securevault.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securevault.dto.ApiResponse;
import com.securevault.dto.LoginRequest;
import com.securevault.dto.LoginResponse;
import com.securevault.dto.RegisterRequest;
import com.securevault.dto.RegisterResponse;
import com.securevault.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

@PostMapping("/register")
public ApiResponse<RegisterResponse> registerUser(
        @Valid @RequestBody RegisterRequest registerRequest) {

    RegisterResponse response = userService.registerUser(registerRequest);

    return new ApiResponse<>(
            true,
            "User registered successfully",
            response
    );
}
@PostMapping("/login")
public ApiResponse<LoginResponse> loginUser(
        @Valid @RequestBody LoginRequest loginRequest) {

    LoginResponse response = userService.loginUser(loginRequest);

    return new ApiResponse<>(
            true,
            "Login successful",
            response
    );
}

}