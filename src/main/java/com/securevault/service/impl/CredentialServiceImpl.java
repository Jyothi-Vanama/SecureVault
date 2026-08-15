package com.securevault.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.securevault.dto.CredentialRequest;
import com.securevault.dto.CredentialResponse;
import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import com.securevault.entity.PasswordHistory;
import com.securevault.entity.User;
import com.securevault.exception.PasswordReuseException;
import com.securevault.exception.ResourceNotFoundException;
import com.securevault.repository.CredentialRepository;
import com.securevault.repository.PasswordHistoryRepository;
import com.securevault.security.AESUtil;
import com.securevault.service.AuditLogService;
import com.securevault.service.CredentialService;
import com.securevault.service.CredentialShareService;
import com.securevault.service.PasswordService;
import com.securevault.specification.CredentialSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.securevault.dto.PasswordStrengthRequest;
import com.securevault.dto.PasswordStrengthResponse;


@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private static final Logger logger =
            LoggerFactory.getLogger(CredentialServiceImpl.class);

    private final CredentialRepository credentialRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final AuditLogService auditLogService;
    private final CredentialShareService credentialShareService;
    private final PasswordService passwordService;


    // =========================================================
    // CREATE CREDENTIAL
    // =========================================================

    @Override
    @Transactional
    public CredentialResponse saveCredential(
            CredentialRequest credentialRequest) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Credential credential = new Credential();

        credential.setTitle(credentialRequest.getTitle());
        credential.setWebsite(credentialRequest.getWebsite());
        credential.setUsername(credentialRequest.getUsername());

        credential.setEncryptedPassword(
                AESUtil.encrypt(credentialRequest.getPassword()));

        credential.setNotes(credentialRequest.getNotes());
        credential.setCategory(credentialRequest.getCategory());
        credential.setUser(user);

        Credential savedCredential =
                credentialRepository.save(credential);

        logger.info(
                "Credential created successfully: credentialId={}, userId={}",
                savedCredential.getCredentialId(),
                user.getUserId()
        );

        auditLogService.saveAuditLog(
                "CREATE",
                "Credential",
                savedCredential.getCredentialId(),
                user
        );

        CredentialResponse response =
                new CredentialResponse();

        response.setCredentialId(
                savedCredential.getCredentialId());

        response.setTitle(savedCredential.getTitle());
        response.setWebsite(savedCredential.getWebsite());
        response.setUsername(savedCredential.getUsername());
        response.setEncryptedPassword(
                savedCredential.getEncryptedPassword());
        response.setNotes(savedCredential.getNotes());
        response.setCategory(savedCredential.getCategory());

        return response;
    }


    // =========================================================
    // GET CREDENTIAL BY ID
    // OWNER OR SHARED USER
    // =========================================================

    @Override
    @Cacheable(
            value = "credentialDetails",
            key = "#id + ':' + T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getPrincipal().getUserId()"
    )
    public CredentialResponse getCredentialById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Credential credential = credentialRepository
                .findByCredentialIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Credential not found"));

        if (!credentialShareService.canAccessCredential(
                credential, user)) {

            throw new AccessDeniedException(
                    "You are not authorized to view this credential");
        }

        logger.debug(
                "Credential accessed: credentialId={}, userId={}",
                id,
                user.getUserId()
        );

        CredentialResponse response =
                new CredentialResponse();

        response.setCredentialId(
                credential.getCredentialId());

        response.setTitle(credential.getTitle());
        response.setWebsite(credential.getWebsite());
        response.setUsername(credential.getUsername());

        response.setEncryptedPassword(
                AESUtil.decrypt(
                        credential.getEncryptedPassword()));

        response.setNotes(credential.getNotes());
        response.setCategory(credential.getCategory());

        return response;
    }


    // =========================================================
    // SEARCH
    // =========================================================

    @Override
    public List<CredentialResponse> searchCredentials(
            String keyword) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        List<Credential> credentials =
                credentialRepository.searchCredentials(
                        user,
                        keyword);

        List<CredentialResponse> responses =
                new ArrayList<>();

        for (Credential credential : credentials) {

            CredentialResponse response =
                    new CredentialResponse();

            response.setCredentialId(
                    credential.getCredentialId());

            response.setTitle(credential.getTitle());
            response.setWebsite(credential.getWebsite());
            response.setUsername(credential.getUsername());

            response.setEncryptedPassword(
                    AESUtil.decrypt(
                            credential.getEncryptedPassword()));

            response.setNotes(credential.getNotes());
            response.setCategory(credential.getCategory());

            responses.add(response);
        }

        return responses;
    }


    // =========================================================
    // FILTER + PAGINATION
    // OWNER CREDENTIALS ONLY
    // =========================================================

    @Override
    public Page<CredentialResponse> getFilteredCredentials(
            Category category,
            String title,
            String username,
            String website,
            Pageable pageable) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        Specification<Credential> specification =
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.and(
                                criteriaBuilder.equal(
                                        root.get("user"),
                                        user),
                                criteriaBuilder.isFalse(
                                        root.get("deleted"))
                        );

        if (category != null) {

            specification = specification.and(
                    CredentialSpecification.hasCategory(
                            category));
        }

        if (title != null && !title.isBlank()) {

            specification = specification.and(
                    CredentialSpecification.hasTitle(
                            title));
        }

        if (username != null && !username.isBlank()) {

            specification = specification.and(
                    CredentialSpecification.hasUsername(
                            username));
        }

        if (website != null && !website.isBlank()) {

            specification = specification.and(
                    CredentialSpecification.hasWebsite(
                            website));
        }

        Page<Credential> credentials =
                credentialRepository.findAll(
                        specification,
                        pageable);

        return credentials.map(credential -> {

            CredentialResponse response =
                    new CredentialResponse();

            response.setCredentialId(
                    credential.getCredentialId());

            response.setTitle(credential.getTitle());
            response.setWebsite(credential.getWebsite());
            response.setUsername(credential.getUsername());

           String decryptedPassword =
        AESUtil.decrypt(
                credential.getEncryptedPassword());

response.setEncryptedPassword(decryptedPassword);

PasswordStrengthRequest strengthRequest =
        new PasswordStrengthRequest();

strengthRequest.setPassword(decryptedPassword);

PasswordStrengthResponse strengthResponse =
        passwordService.analyzePassword(strengthRequest);

response.setStrength(
        strengthResponse.getStrength());

            response.setNotes(credential.getNotes());
            response.setCategory(credential.getCategory());

            return response;
        });
    }


    // =========================================================
    // UPDATE CREDENTIAL
    // OWNER OR EDIT SHARED USER
    // =========================================================

    @Override
    @Transactional
    @CacheEvict(
            value = "credentialDetails",
            allEntries = true
    )
    public CredentialResponse updateCredential(
            Long id,
            CredentialRequest credentialRequest) {

        Credential credential = credentialRepository
                .findByCredentialIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Credential not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        if (!credentialShareService.canEditCredential(
                id,
                user)) {

            throw new AccessDeniedException(
                    "You do not have permission to edit this credential");
        }


        // -----------------------------------------------------
        // UPDATE NORMAL FIELDS
        // -----------------------------------------------------

        credential.setTitle(
                credentialRequest.getTitle());

        credential.setWebsite(
                credentialRequest.getWebsite());

        credential.setUsername(
                credentialRequest.getUsername());


        // -----------------------------------------------------
        // PASSWORD CHANGE
        // -----------------------------------------------------

        String existingPassword =
                AESUtil.decrypt(
                        credential.getEncryptedPassword());

        boolean passwordChanged =
                !existingPassword.equals(
                        credentialRequest.getPassword());


        if (passwordChanged) {

            List<PasswordHistory> recentHistories =
                    passwordHistoryRepository
                            .findTop5ByCredentialCredentialIdOrderByVersionDesc(
                                    credential.getCredentialId());


            // -------------------------------------------------
            // PASSWORD REUSE CHECK
            // -------------------------------------------------

            for (PasswordHistory oldHistory :
                    recentHistories) {

                String oldPassword =
                        AESUtil.decrypt(
                                oldHistory.getEncryptedPassword());

                if (oldPassword.equals(
                        credentialRequest.getPassword())) {

                    throw new PasswordReuseException(
                            "Password was recently used");
                }
            }


            // -------------------------------------------------
            // CREATE PASSWORD HISTORY
            // -------------------------------------------------

            PasswordHistory history =
                    new PasswordHistory();

            history.setCredential(credential);

            history.setEncryptedPassword(
                    credential.getEncryptedPassword());

            int nextVersion =
                    passwordHistoryRepository
                            .findTopByCredentialCredentialIdOrderByVersionDesc(
                                    credential.getCredentialId())
                            .map(latestHistory ->
                                    latestHistory.getVersion() + 1)
                            .orElse(1);

            history.setVersion(nextVersion);

            history.setCreatedAt(
                    LocalDateTime.now());

            passwordHistoryRepository.save(history);


            // -------------------------------------------------
            // SAVE NEW ENCRYPTED PASSWORD
            // -------------------------------------------------

            credential.setEncryptedPassword(
                    AESUtil.encrypt(
                            credentialRequest.getPassword()));

            logger.info(
                    "Credential password changed: credentialId={}, userId={}",
                    credential.getCredentialId(),
                    user.getUserId()
            );
        }


        credential.setNotes(
                credentialRequest.getNotes());

        credential.setCategory(
                credentialRequest.getCategory());


        Credential updatedCredential =
                credentialRepository.save(credential);

        logger.info(
                "Credential updated successfully: credentialId={}, userId={}",
                updatedCredential.getCredentialId(),
                user.getUserId()
        );


        auditLogService.saveAuditLog(
                "UPDATE",
                "Credential",
                updatedCredential.getCredentialId(),
                user
        );


        CredentialResponse response =
                new CredentialResponse();

        response.setCredentialId(
                updatedCredential.getCredentialId());

        response.setTitle(
                updatedCredential.getTitle());

        response.setWebsite(
                updatedCredential.getWebsite());

        response.setUsername(
                updatedCredential.getUsername());

        response.setEncryptedPassword(
                updatedCredential.getEncryptedPassword());

        response.setNotes(
                updatedCredential.getNotes());

        response.setCategory(
                updatedCredential.getCategory());

        return response;
    }


    // =========================================================
    // TRASH
    // =========================================================

    @Override
    public List<CredentialResponse> getDeletedCredentials() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User user =
                (User) authentication.getPrincipal();

        List<Credential> credentials =
                credentialRepository
                        .findByUserAndDeletedTrue(user);

        return credentials.stream()
                .map(credential -> {

                    CredentialResponse response =
                            new CredentialResponse();

                    response.setCredentialId(
                            credential.getCredentialId());

                    response.setTitle(
                            credential.getTitle());

                    response.setWebsite(
                            credential.getWebsite());

                    response.setUsername(
                            credential.getUsername());

                    response.setEncryptedPassword(
                            AESUtil.decrypt(
                                    credential.getEncryptedPassword()));

                    response.setNotes(
                            credential.getNotes());

                    response.setCategory(
                            credential.getCategory());

                    return response;
                })
                .toList();
    }


    // =========================================================
    // RESTORE
    // OWNER ONLY
    // =========================================================

    @Override
    @Transactional
    @CacheEvict(
            value = "credentialDetails",
            allEntries = true
    )
    public void restoreCredential(Long id) {

        Credential credential =
                credentialRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Credential not found"));

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User user =
                (User) authentication.getPrincipal();

        if (!credential.getUser()
                .getUserId()
                .equals(user.getUserId())) {

            throw new AccessDeniedException(
                    "Only the owner can restore this credential");
        }

        credential.setDeleted(false);
        credential.setDeletedAt(null);

        credentialRepository.save(credential);

        logger.info(
                "Credential restored successfully: credentialId={}, userId={}",
                id,
                user.getUserId()
        );
    }


    // =========================================================
    // SOFT DELETE
    // OWNER ONLY
    // =========================================================

    @Override
    @Transactional
    @CacheEvict(
            value = "credentialDetails",
            allEntries = true
    )
    public void deleteCredential(Long id) {

        Credential credential =
                credentialRepository
                        .findByCredentialIdAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Credential not found"));

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User user =
                (User) authentication.getPrincipal();

        if (!credential.getUser()
                .getUserId()
                .equals(user.getUserId())) {

            throw new AccessDeniedException(
                    "Only the owner can delete this credential");
        }

        credential.setDeleted(true);

        credential.setDeletedAt(
                LocalDateTime.now());

        credentialRepository.save(credential);

        auditLogService.saveAuditLog(
                "DELETE",
                "Credential",
                credential.getCredentialId(),
                user
        );

        logger.info(
                "Credential soft-deleted: credentialId={}, userId={}",
                credential.getCredentialId(),
                user.getUserId()
        );
    }


    // =========================================================
    // PERMANENT DELETE
    // OWNER ONLY
    // =========================================================

    @Override
    @Transactional
    @CacheEvict(
            value = "credentialDetails",
            allEntries = true
    )
    public void permanentlyDeleteCredential(Long id) {

        Credential credential =
                credentialRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Credential not found"));

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User user =
                (User) authentication.getPrincipal();

        if (!credential.getUser()
                .getUserId()
                .equals(user.getUserId())) {

            throw new AccessDeniedException(
                    "Only the owner can permanently delete this credential");
        }

        logger.info(
                "Credential permanently deleted: credentialId={}, userId={}",
                id,
                user.getUserId()
        );

        credentialRepository.delete(credential);
    }
}