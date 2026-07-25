package com.securevault.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialResponse {

    private Long credentialId;
    private String website;
    private String username;
    private String encryptedPassword;
    private String title;
    private String notes;
}