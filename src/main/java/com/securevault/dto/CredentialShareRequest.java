package com.securevault.dto;

import java.time.LocalDateTime;

import com.securevault.entity.Permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CredentialShareRequest {

    private Long credentialId;

    private Long sharedWithUserId;

    private Permission permission;

    private LocalDateTime expiresAt;
}