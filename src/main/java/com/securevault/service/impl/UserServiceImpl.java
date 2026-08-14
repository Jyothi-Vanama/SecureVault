package com.securevault.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.LoginResponse;
import com.securevault.dto.RegisterRequest;
import com.securevault.dto.RegisterResponse;
import com.securevault.repository.UserRepository;
import com.securevault.security.JwtUtil;
import com.securevault.service.UserService;
import com.securevault.entity.User;
import com.securevault.exception.DuplicateEmailException;
import com.securevault.exception.InvalidCredentialsException;
import com.securevault.exception.UserNotFoundException;
import org.springframework.cache.annotation.Cacheable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    private final JwtUtil jwtUtil = new JwtUtil();


    // =========================================================
    // USER REGISTRATION
    // =========================================================

    @Override
    public RegisterResponse registerUser(
            RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(
                registerRequest.getEmail())) {

            logger.warn(
                    "Registration failed - email already exists: {}",
                    registerRequest.getEmail()
            );

            throw new DuplicateEmailException(
                    "Email already exists.");
        }

        User user = new User();

        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        registerRequest.getPassword())
        );

        User savedUser =
                userRepository.save(user);

        logger.info(
                "User registered successfully: {}",
                savedUser.getEmail()
        );

        RegisterResponse response =
                new RegisterResponse();

        response.setUserId(
                savedUser.getUserId());

        response.setName(
                savedUser.getName());

        response.setEmail(
                savedUser.getEmail());

        return response;
    }


    // =========================================================
    // LOGIN
    // =========================================================

    @Override
    public LoginResponse loginUser(
            LoginRequest loginRequest) {

        User user =
                userRepository
                        .findByEmail(
                                loginRequest.getEmail())
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Login failed - user not found: {}",
                                    loginRequest.getEmail()
                            );

                            return new UserNotFoundException(
                                    "User not found");
                        });


        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            logger.warn(
                    "Login failed - invalid credentials for: {}",
                    loginRequest.getEmail()
            );

            throw new InvalidCredentialsException(
                    "Invalid email or password");
        }


        String token =
                jwtUtil.generateToken(
                        user.getEmail());


        logger.info(
                "Login successful for user: {}",
                user.getEmail()
        );

        // Never log the JWT token.

        return new LoginResponse(token);
    }


    // =========================================================
    // FIND USER BY EMAIL
    // =========================================================

    @Override
@Cacheable(value = "userProfiles", key = "#email")
public User findByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));
    }
}