package com.securevault.service;

import com.securevault.dto.PasswordGeneratorRequest;
import com.securevault.dto.PasswordGeneratorResponse;
import com.securevault.dto.PasswordStrengthRequest;
import com.securevault.dto.PasswordStrengthResponse;

public interface PasswordService {

    PasswordStrengthResponse analyzePassword(
            PasswordStrengthRequest request);

    PasswordGeneratorResponse generatePassword(
            PasswordGeneratorRequest request);

}