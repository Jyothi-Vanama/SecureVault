package com.securevault.service;

import java.util.List;

import com.securevault.dto.CredentialResponse;
import com.securevault.dto.CredentialShareRequest;
import com.securevault.dto.PermissionUpdateRequest;
import com.securevault.entity.User;
import com.securevault.entity.Credential;

public interface CredentialShareService {

    void shareCredential(CredentialShareRequest request);

    List<CredentialResponse> getReceivedShares();

   boolean canAccessCredential(Credential credential, User user);

    boolean canEditCredential(Long credentialId, User user);

    boolean isOwner(Long credentialId, User user);
    void updatePermission(Long shareId, PermissionUpdateRequest request);
    void revokeShare(Long shareId);
}