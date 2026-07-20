package com.securevault.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.securevault.entity.Credential;
import com.securevault.service.CredentialService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    @PostMapping
    public Credential saveCredential(@RequestBody Credential credential) {

        return credentialService.saveCredential(credential);

    }
    @GetMapping("/{id}")
public Credential getCredentialById(@PathVariable Long id) {

    return credentialService.getCredentialById(id);
}
@PutMapping("/{id}")
public Credential updateCredential(@PathVariable Long id,
                                   @RequestBody Credential updatedCredential) {

    return credentialService.updateCredential(id, updatedCredential);

}
@DeleteMapping("/{id}")
public String deleteCredential(@PathVariable Long id) {

    credentialService.deleteCredential(id);

    return "Credential deleted successfully.";

}

}