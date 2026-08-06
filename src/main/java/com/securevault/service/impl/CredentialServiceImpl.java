package com.securevault.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import com.securevault.entity.User;
import com.securevault.exception.ResourceNotFoundException;
import com.securevault.repository.CredentialRepository;
import com.securevault.security.AESUtil;
import com.securevault.service.CredentialService;
import com.securevault.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import com.securevault.entity.AuditLog;
import com.securevault.repository.AuditLogRepository;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final AuditLogService auditLogService;
    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public CredentialResponse saveCredential(CredentialRequest credentialRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Credential credential = new Credential();
        credential.setTitle(credentialRequest.getTitle());
        credential.setWebsite(credentialRequest.getWebsite());
        credential.setUsername(credentialRequest.getUsername());
        credential.setEncryptedPassword(AESUtil.encrypt(credentialRequest.getPassword()));
        credential.setNotes(credentialRequest.getNotes());
        credential.setCategory(credentialRequest.getCategory());
        credential.setUser(user);

        Credential savedCredential = credentialRepository.save(credential);
auditLogService.saveAuditLog(
        "CREATE",
        "Credential",
        savedCredential.getCredentialId(),
        user
);

        CredentialResponse response = new CredentialResponse();
        response.setCredentialId(savedCredential.getCredentialId());
        response.setTitle(savedCredential.getTitle());
        response.setWebsite(savedCredential.getWebsite());
        response.setUsername(savedCredential.getUsername());
        response.setEncryptedPassword(savedCredential.getEncryptedPassword());
        response.setNotes(savedCredential.getNotes());
        response.setCategory(savedCredential.getCategory());

        return response;
    }

    @Override
    public CredentialResponse getCredentialById(Long id) {

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        CredentialResponse response = new CredentialResponse();
        response.setCredentialId(credential.getCredentialId());
        response.setTitle(credential.getTitle());
        response.setWebsite(credential.getWebsite());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(
                AESUtil.decrypt(credential.getEncryptedPassword()));
        response.setNotes(credential.getNotes());
        response.setCategory(credential.getCategory());
        return response;
    }

    @Override
public List<CredentialResponse> getAllCredentials() {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    List<Credential> credentials = credentialRepository.findByUser(user);

    List<CredentialResponse> responses = new ArrayList<>();

    for (Credential credential : credentials) {

        CredentialResponse response = new CredentialResponse();

        response.setCredentialId(credential.getCredentialId());
        response.setTitle(credential.getTitle());
        response.setWebsite(credential.getWebsite());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(
                AESUtil.decrypt(credential.getEncryptedPassword()));
        response.setNotes(credential.getNotes());
        response.setCategory(credential.getCategory());
        responses.add(response);
    }

    return responses;
}

@Override
public List<CredentialResponse> searchCredentials(String keyword) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    List<Credential> credentials = credentialRepository.searchCredentials(user, keyword);

    List<CredentialResponse> responses = new ArrayList<>();

    for (Credential credential : credentials) {

        CredentialResponse response = new CredentialResponse();

        response.setCredentialId(credential.getCredentialId());
        response.setTitle(credential.getTitle());
        response.setWebsite(credential.getWebsite());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(
                AESUtil.decrypt(credential.getEncryptedPassword()));
        response.setNotes(credential.getNotes());
        response.setCategory(credential.getCategory());

        responses.add(response);
    }

    return responses;
}

@Override
public List<CredentialResponse> getCredentialsByCategory(Category category) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    List<Credential> credentials =
            credentialRepository.findByUserAndCategory(user, category);

    List<CredentialResponse> responses = new ArrayList<>();

    for (Credential credential : credentials) {

        CredentialResponse response = new CredentialResponse();

        response.setCredentialId(credential.getCredentialId());
        response.setTitle(credential.getTitle());
        response.setWebsite(credential.getWebsite());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(
                AESUtil.decrypt(credential.getEncryptedPassword()));
        response.setNotes(credential.getNotes());
        response.setCategory(credential.getCategory());

        responses.add(response);
    }

    return responses;
}

    @Override
    public CredentialResponse updateCredential(Long id, CredentialRequest credentialRequest) {

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User user = (User) authentication.getPrincipal();

if (!credential.getUser().getUserId().equals(user.getUserId())) {
    throw new RuntimeException("You are not authorized to delete this credential");
}

        credential.setTitle(credentialRequest.getTitle());
        credential.setWebsite(credentialRequest.getWebsite());
        credential.setUsername(credentialRequest.getUsername());
        String existingPassword = AESUtil.decrypt(credential.getEncryptedPassword());

if (!existingPassword.equals(credentialRequest.getPassword())) {
    credential.setEncryptedPassword(
            AESUtil.encrypt(credentialRequest.getPassword()));
}
        credential.setNotes(credentialRequest.getNotes());
credential.setCategory(credentialRequest.getCategory());

        Credential updatedCredential = credentialRepository.save(credential);

        CredentialResponse response = new CredentialResponse();
        response.setCredentialId(updatedCredential.getCredentialId());
        response.setTitle(updatedCredential.getTitle());
        response.setWebsite(updatedCredential.getWebsite());
        response.setUsername(updatedCredential.getUsername());
        response.setEncryptedPassword(updatedCredential.getEncryptedPassword());
        response.setNotes(updatedCredential.getNotes());
        response.setCategory(updatedCredential.getCategory());

        return response;
    }

    @Override
    public void deleteCredential(Long id) {

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Credential not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User user = (User) authentication.getPrincipal();

if (!credential.getUser().getUserId().equals(user.getUserId())) {
    throw new RuntimeException("You are not authorized to delete this credential");
}

        credentialRepository.delete(credential);
    }
}