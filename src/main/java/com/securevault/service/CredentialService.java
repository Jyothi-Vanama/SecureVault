package com.securevault.service;

import com.securevault.entity.Credential;

public interface CredentialService {

    Credential saveCredential(Credential credential);

    Credential getCredentialById(Long id);

    Credential updateCredential(Long id, Credential updatedCredential);

    void deleteCredential(Long id);

}