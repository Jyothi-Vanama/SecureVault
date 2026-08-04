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

import com.securevault.dto.ApiResponse;
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
public ApiResponse<CredentialResponse> saveCredential(
        @Valid @RequestBody CredentialRequest credentialRequest) {

    CredentialResponse response =
            credentialService.saveCredential(credentialRequest);

    return new ApiResponse<>(
            true,
            "Credential created successfully",
            response
    );
}

    @GetMapping("/{id}")
public ApiResponse<CredentialResponse> getCredentialById(
        @PathVariable Long id) {

    CredentialResponse response =
            credentialService.getCredentialById(id);

    return new ApiResponse<>(
            true,
            "Credential fetched successfully",
            response
    );
}
@GetMapping
public ApiResponse<List<CredentialResponse>> getAllCredentials(
        @RequestParam(required = false) Category category) {

    List<CredentialResponse> response;

    if (category != null) {
        response = credentialService.getCredentialsByCategory(category);
    } else {
        response = credentialService.getAllCredentials();
    }

    return new ApiResponse<>(
            true,
            "Credentials fetched successfully",
            response
    );
}

    @PutMapping("/{id}")
public ApiResponse<CredentialResponse> updateCredential(
        @PathVariable Long id,
        @Valid @RequestBody CredentialRequest credentialRequest) {

    CredentialResponse response =
            credentialService.updateCredential(id, credentialRequest);

    return new ApiResponse<>(
            true,
            "Credential updated successfully",
            response
    );
}
@GetMapping("/search")
public ApiResponse<List<CredentialResponse>> searchCredentials(
        @RequestParam String keyword) {

    List<CredentialResponse> response =
            credentialService.searchCredentials(keyword);

    return new ApiResponse<>(
            true,
            "Search completed successfully",
            response
    );
}
    @DeleteMapping("/{id}")
public ApiResponse<String> deleteCredential(@PathVariable Long id) {

    credentialService.deleteCredential(id);

    return new ApiResponse<>(
            true,
            "Credential deleted successfully",
            null
    );
}
}