package com.securevault.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Category;

public interface CredentialService {

    CredentialResponse saveCredential(
            CredentialRequest credentialRequest);

    CredentialResponse getCredentialById(Long id);

    List<CredentialResponse> searchCredentials(
            String keyword);

    Page<CredentialResponse> getFilteredCredentials(
            Category category,
            String title,
            String username,
            String website,
            Pageable pageable);

    CredentialResponse updateCredential(
            Long id,
            CredentialRequest credentialRequest);

    void deleteCredential(Long id);
}