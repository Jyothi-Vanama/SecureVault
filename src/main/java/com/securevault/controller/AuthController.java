package com.securevault.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.LoginResponse;
import com.securevault.entity.User;
import com.securevault.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {

        return userService.registerUser(user);

    }
    @PostMapping("/login")
public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {

    return userService.loginUser(loginRequest);

}

}