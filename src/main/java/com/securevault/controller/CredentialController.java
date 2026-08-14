package com.securevault.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.securevault.service.AsyncTaskService;
import com.securevault.service.CredentialService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/vault")
@RequiredArgsConstructor
public class CredentialController {

    private static final Logger logger =
            LoggerFactory.getLogger(CredentialController.class);

    private final CredentialService credentialService;
    private final AsyncTaskService asyncTaskService;

    @PostMapping
    public ApiResponse<CredentialResponse> saveCredential(
            @Valid @RequestBody CredentialRequest credentialRequest) {

        CredentialResponse response =
                credentialService.saveCredential(credentialRequest);

        logger.info(
                "Credential creation request handled on thread: {}",
                Thread.currentThread().getName()
        );

        asyncTaskService.sendEmailNotification();

        asyncTaskService.logActivity(
                response.getCredentialId(),
                "CREATE"
        );

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
    public ApiResponse<Page<CredentialResponse>> getAllCredentials(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String website,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Pageable pageable) {

        List<String> allowedSortFields = List.of(
                "title",
                "username",
                "website",
                "category",
                "createdAt",
                "updatedAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "title";
        }

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(sortDirection, sortBy)
        );

        Page<CredentialResponse> response =
                credentialService.getFilteredCredentials(
                        category,
                        title,
                        username,
                        website,
                        pageable
                );

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
                credentialService.updateCredential(
                        id,
                        credentialRequest
                );

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

    @PutMapping("/{id}/restore")
    public ApiResponse<String> restoreCredential(
            @PathVariable Long id) {

        credentialService.restoreCredential(id);

        return new ApiResponse<>(
                true,
                "Credential restored successfully",
                null
        );
    }

    @GetMapping("/trash")
    public ApiResponse<List<CredentialResponse>> getTrash() {

        List<CredentialResponse> response =
                credentialService.getDeletedCredentials();

        return new ApiResponse<>(
                true,
                "Trash fetched successfully",
                response
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCredential(
            @PathVariable Long id) {

        credentialService.deleteCredential(id);

        return new ApiResponse<>(
                true,
                "Credential deleted successfully",
                null
        );
    }

    @DeleteMapping("/{id}/permanent")
    public ApiResponse<String> permanentlyDeleteCredential(
            @PathVariable Long id) {

        credentialService.permanentlyDeleteCredential(id);

        return new ApiResponse<>(
                true,
                "Credential permanently deleted successfully",
                null
        );
    }
}