package com.securevault.service;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.LoginResponse;
import com.securevault.dto.RegisterRequest;
import com.securevault.dto.RegisterResponse;
import com.securevault.entity.User;

public interface UserService {

    RegisterResponse registerUser(RegisterRequest registerRequest);

    LoginResponse loginUser(LoginRequest loginRequest);

    User findByEmail(String email);
}