package com.securevault.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Category;
import com.securevault.service.CredentialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;

    @PostMapping
    public CredentialResponse saveCredential(@Valid @RequestBody CredentialRequest credentialRequest) {

        return credentialService.saveCredential(credentialRequest);
    }

    @GetMapping("/{id}")
    public CredentialResponse getCredentialById(@PathVariable Long id) {

        return credentialService.getCredentialById(id);
    }
 @GetMapping
public List<CredentialResponse> getAllCredentials(
        @RequestParam(required = false) Category category) {

    if (category != null) {
        return credentialService.getCredentialsByCategory(category);
    }

    return credentialService.getAllCredentials();
}

    @PutMapping("/{id}")
    public CredentialResponse updateCredential(
            @PathVariable Long id,
            @Valid @RequestBody CredentialRequest credentialRequest) {

        return credentialService.updateCredential(id, credentialRequest);
    }
@GetMapping("/search")
public List<CredentialResponse> searchCredentials(
        @RequestParam String keyword) {

    return credentialService.searchCredentials(keyword);
}
    @DeleteMapping("/{id}")
    public String deleteCredential(@PathVariable Long id) {

        credentialService.deleteCredential(id);

        return "Credential deleted successfully.";
    }
}