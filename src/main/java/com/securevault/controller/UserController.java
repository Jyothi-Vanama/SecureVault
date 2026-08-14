package com.securevault.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securevault.entity.User;
import com.securevault.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public User getProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User user = (User) authentication.getPrincipal();

        return userService.findByEmail(user.getEmail());
    }
}