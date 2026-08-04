package com.securevault.service.impl;

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

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil = new JwtUtil();

    @Override
public RegisterResponse registerUser(RegisterRequest registerRequest) {

    if (userRepository.existsByEmail(registerRequest.getEmail())) {
        throw new DuplicateEmailException("Email already exists.");
    }

    User user = new User();
    user.setName(registerRequest.getName());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

    User savedUser = userRepository.save(user);

    RegisterResponse response = new RegisterResponse();
    response.setUserId(savedUser.getUserId());
    response.setName(savedUser.getName());
    response.setEmail(savedUser.getEmail());

    return response;
}
    @Override
public LoginResponse loginUser(LoginRequest loginRequest) {

    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new InvalidCredentialsException("Invalid email or password");
    }

    String token = jwtUtil.generateToken(user.getEmail());

return new LoginResponse(token);
}

@Override
public User findByEmail(String email) {

    return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found"));

}

}