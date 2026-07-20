package com.securevault.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.LoginResponse;
import com.securevault.entity.User;
import com.securevault.repository.UserRepository;
import com.securevault.security.JwtUtil;
import com.securevault.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil = new JwtUtil();

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
    throw new RuntimeException("Email already exists.");
}
    user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }
    @Override
public LoginResponse loginUser(LoginRequest loginRequest) {

    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new RuntimeException("Invalid Password");
    }

    String token = jwtUtil.generateToken(user.getEmail());

return new LoginResponse(token);
}

}