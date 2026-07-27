package com.securevault.service;

import java.util.List;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Category;

public interface CredentialService {

    CredentialResponse saveCredential(CredentialRequest credentialRequest);

    CredentialResponse getCredentialById(Long id);
    List<CredentialResponse> getAllCredentials();
List<CredentialResponse> searchCredentials(String keyword);
    CredentialResponse updateCredential(Long id, CredentialRequest credentialRequest);
    List<CredentialResponse> getCredentialsByCategory(Category category);

    void deleteCredential(Long id);
}