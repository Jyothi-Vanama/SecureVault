package com.securevault.service.impl;

import org.springframework.stereotype.Service;

import com.securevault.entity.Credential;
import com.securevault.entity.User;
import com.securevault.repository.CredentialRepository;
import com.securevault.repository.UserRepository;
import com.securevault.security.AESUtil;
import com.securevault.service.CredentialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;

    @Override
    public Credential saveCredential(Credential credential) {

        User user = userRepository.findById(5L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        credential.setUser(user);

        credential.setPassword(AESUtil.encrypt(credential.getPassword()));

return credentialRepository.save(credential);
    }
    @Override
public Credential getCredentialById(Long id) {

    Credential credential = credentialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credential not found"));

    credential.setPassword(AESUtil.decrypt(credential.getPassword()));

    return credential;
}

@Override
public Credential updateCredential(Long id, Credential updatedCredential) {

    Credential existingCredential = credentialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credential not found"));

    existingCredential.setWebsite(updatedCredential.getWebsite());
    existingCredential.setUsername(updatedCredential.getUsername());

    existingCredential.setPassword(
            AESUtil.encrypt(updatedCredential.getPassword()));

    return credentialRepository.save(existingCredential);
}

@Override
public void deleteCredential(Long id) {

    Credential credential = credentialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Credential not found"));

    credentialRepository.delete(credential);

}

}