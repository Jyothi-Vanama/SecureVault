package com.securevault.service;

import com.securevault.dto.LoginRequest;
import com.securevault.dto.LoginResponse;
import com.securevault.entity.User;

public interface UserService {

    User registerUser(User user);
    LoginResponse loginUser(LoginRequest loginRequest);

    User findByEmail(String email);

}