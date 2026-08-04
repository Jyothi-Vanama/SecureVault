package com.securevault.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordStrengthRequest {

    @NotBlank(message = "Password cannot be empty")
    private String password;

}