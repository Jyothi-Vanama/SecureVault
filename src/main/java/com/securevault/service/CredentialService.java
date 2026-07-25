package com.securevault.service;

import java.util.List;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;

public interface CredentialService {

    CredentialResponse saveCredential(CredentialRequest credentialRequest);

    CredentialResponse getCredentialById(Long id);
    List<CredentialResponse> getAllCredentials();

    CredentialResponse updateCredential(Long id, CredentialRequest credentialRequest);

    void deleteCredential(Long id);
}