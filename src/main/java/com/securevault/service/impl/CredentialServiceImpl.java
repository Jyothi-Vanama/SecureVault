package com.securevault.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Credential;
import com.securevault.entity.User;
import com.securevault.repository.CredentialRepository;
import com.securevault.security.AESUtil;
import com.securevault.service.CredentialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;

    @Override
    public CredentialResponse saveCredential(CredentialRequest credentialRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Credential credential = new Credential();
        credential.setTitle(credentialRequest.getTitle());
        credential.setWebsite(credentialRequest.getWebsite());
        credential.setUsername(credentialRequest.getUsername());
        credential.setEncryptedPassword(AESUtil.encrypt(credentialRequest.getPassword()));
        credential.setNotes(credentialRequest.getNotes());
        credential.setUser(user);

        Credential savedCredential = credentialRepository.save(credential);

        CredentialResponse response = new CredentialResponse();
        response.setCredentialId(savedCredential.getCredentialId());
        response.setTitle(savedCredential.getTitle());
        response.setWebsite(savedCredential.getWebsite());
        response.setUsername(savedCredential.getUsername());
        response.setEncryptedPassword(savedCredential.getEncryptedPassword());
        response.setNotes(savedCredential.getNotes());

        return response;
    }

    @Override
    public CredentialResponse getCredentialById(Long id) {

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        CredentialResponse response = new CredentialResponse();
        response.setCredentialId(credential.getCredentialId());
        response.setTitle(credential.getTitle());
        response.setWebsite(credential.getWebsite());
        response.setUsername(credential.getUsername());
        response.setEncryptedPassword(
                AESUtil.decrypt(credential.getEncryptedPassword()));
        response.setNotes(credential.getNotes());

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

        responses.add(response);
    }

    return responses;
}

    @Override
    public CredentialResponse updateCredential(Long id, CredentialRequest credentialRequest) {

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User user = (User) authentication.getPrincipal();

if (!credential.getUser().getUserId().equals(user.getUserId())) {
    throw new RuntimeException("You are not authorized to update this credential");
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

        Credential updatedCredential = credentialRepository.save(credential);

        CredentialResponse response = new CredentialResponse();
        response.setCredentialId(updatedCredential.getCredentialId());
        response.setTitle(updatedCredential.getTitle());
        response.setWebsite(updatedCredential.getWebsite());
        response.setUsername(updatedCredential.getUsername());
        response.setEncryptedPassword(updatedCredential.getEncryptedPassword());
        response.setNotes(updatedCredential.getNotes());

        return response;
    }

    @Override
    public void deleteCredential(Long id) {

        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Credential not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User user = (User) authentication.getPrincipal();

if (!credential.getUser().getUserId().equals(user.getUserId())) {
    throw new RuntimeException("You are not authorized to delete this credential");
}

        credentialRepository.delete(credential);
    }
}