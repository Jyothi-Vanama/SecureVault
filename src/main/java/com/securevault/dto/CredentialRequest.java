package com.securevault.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialRequest {

    @NotBlank(message = "Title cannot be empty")
private String title;

    @NotBlank(message = "Website cannot be empty")
    private String website;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;

    private String notes;

}