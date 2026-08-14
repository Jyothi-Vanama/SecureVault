package com.securevault.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.securevault.dto.CredentialShareRequest;
import com.securevault.dto.PermissionUpdateRequest;
import com.securevault.dto.ApiResponse;
import com.securevault.dto.CredentialResponse;
import com.securevault.service.CredentialShareService;
import java.util.List;

@RestController
@RequestMapping("/api/share")
public class CredentialShareController {

    private final CredentialShareService credentialShareService;

    public CredentialShareController(
            CredentialShareService credentialShareService) {
        this.credentialShareService = credentialShareService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Object> shareCredential(
            @RequestBody CredentialShareRequest request) {

        credentialShareService.shareCredential(request);

        return new ApiResponse<>(
                true,
                "Credential shared successfully",
                null
        );
    }

    @GetMapping("/received")
public ApiResponse<List<CredentialResponse>> getReceivedShares() {

    List<CredentialResponse> credentials =
            credentialShareService.getReceivedShares();

    return new ApiResponse<>(
            true,
            "Received shares fetched successfully",
            credentials
    );
}

@PutMapping("/{shareId}")
public ApiResponse<Object> updatePermission(
        @PathVariable Long shareId,
        @RequestBody PermissionUpdateRequest request) {

    credentialShareService.updatePermission(
            shareId,
            request);

    return new ApiResponse<>(
            true,
            "Share permission updated successfully",
            null
    );
}
@DeleteMapping("/{shareId}")
public ApiResponse<Object> revokeShare(
        @PathVariable Long shareId) {

    credentialShareService.revokeShare(shareId);

    return new ApiResponse<>(
            true,
            "Credential sharing revoked successfully",
            null
    );
}
}