package com.securevault.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.securevault.dto.CredentialResponse;
import com.securevault.dto.CredentialShareRequest;
import com.securevault.dto.PermissionUpdateRequest;
import com.securevault.entity.Credential;
import com.securevault.entity.CredentialShare;
import com.securevault.entity.Permission;
import com.securevault.entity.User;
import com.securevault.exception.ResourceNotFoundException;
import com.securevault.repository.CredentialRepository;
import com.securevault.repository.CredentialShareRepository;
import com.securevault.repository.UserRepository;
import com.securevault.security.AESUtil;
import com.securevault.service.CredentialShareService;

@Service
public class CredentialShareServiceImpl
        implements CredentialShareService {

    private static final Logger logger =
            LoggerFactory.getLogger(CredentialShareServiceImpl.class);

    private final CredentialRepository credentialRepository;
    private final CredentialShareRepository credentialShareRepository;
    private final UserRepository userRepository;

    public CredentialShareServiceImpl(
            CredentialRepository credentialRepository,
            CredentialShareRepository credentialShareRepository,
            UserRepository userRepository) {

        this.credentialRepository = credentialRepository;
        this.credentialShareRepository = credentialShareRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void shareCredential(CredentialShareRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        User owner = (User) authentication.getPrincipal();

        Credential credential = credentialRepository
                .findByCredentialIdAndDeletedFalse(
                        request.getCredentialId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Credential not found"));

        if (!credential.getUser()
                .getUserId()
                .equals(owner.getUserId())) {

            logger.warn(
                    "Unauthorized share attempt: credentialId={}, userId={}",
                    request.getCredentialId(),
                    owner.getUserId()
            );

            throw new RuntimeException(
                    "Only the owner can share this credential");
        }

        if (owner.getUserId()
                .equals(request.getSharedWithUserId())) {

            logger.warn(
                    "Share attempt to self: credentialId={}, userId={}",
                    request.getCredentialId(),
                    owner.getUserId()
            );

            throw new RuntimeException(
                    "You cannot share a credential with yourself");
        }

        User sharedWithUser = userRepository
                .findById(request.getSharedWithUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        if (credentialShareRepository
                .existsByCredentialCredentialIdAndSharedWithUserAndActiveTrue(
                        credential.getCredentialId(),
                        sharedWithUser)) {

            logger.warn(
                    "Duplicate share attempt: credentialId={}, ownerId={}, sharedWithUserId={}",
                    credential.getCredentialId(),
                    owner.getUserId(),
                    sharedWithUser.getUserId()
            );

            throw new RuntimeException(
                    "Credential is already shared with this user");
        }

        CredentialShare share = new CredentialShare();

        share.setCredential(credential);
        share.setOwner(owner);
        share.setSharedWithUser(sharedWithUser);
        share.setPermission(request.getPermission());
        share.setSharedAt(LocalDateTime.now());
        share.setExpiresAt(request.getExpiresAt());
        share.setActive(true);

        credentialShareRepository.save(share);

        logger.info(
                "Credential shared successfully: credentialId={}, ownerId={}, sharedWithUserId={}, permission={}",
                credential.getCredentialId(),
                owner.getUserId(),
                sharedWithUser.getUserId(),
                request.getPermission()
        );
    }

    @Override
    @Transactional
    public void updatePermission(
            Long shareId,
            PermissionUpdateRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User owner = (User) authentication.getPrincipal();

        CredentialShare share =
                credentialShareRepository
                        .findById(shareId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Share not found"));

        if (!share.getOwner()
                .getUserId()
                .equals(owner.getUserId())) {

            logger.warn(
                    "Unauthorized share permission update: shareId={}, userId={}",
                    shareId,
                    owner.getUserId()
            );

            throw new AccessDeniedException(
                    "Only the owner can update share permission");
        }

        if (!share.isActive()) {

            logger.warn(
                    "Permission update attempted on revoked share: shareId={}, userId={}",
                    shareId,
                    owner.getUserId()
            );

            throw new RuntimeException(
                    "Share has already been revoked");
        }

        share.setPermission(request.getPermission());

        credentialShareRepository.save(share);

        logger.info(
                "Share permission updated: shareId={}, credentialId={}, userId={}, permission={}",
                shareId,
                share.getCredential().getCredentialId(),
                owner.getUserId(),
                request.getPermission()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CredentialResponse> getReceivedShares() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User user = (User) authentication.getPrincipal();

        List<CredentialShare> shares =
                credentialShareRepository
                        .findBySharedWithUserAndActiveTrue(user);

        return shares.stream()
                .filter(share ->
                        share.getCredential() != null
                        && !share.getCredential().isDeleted()
                        && (share.getExpiresAt() == null
                        || share.getExpiresAt()
                                .isAfter(LocalDateTime.now())))
                .map(share -> {

                    Credential credential =
                            share.getCredential();

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

    @Override
    @Transactional(readOnly = true)
    public boolean isOwner(Long credentialId, User user) {

        Credential credential = credentialRepository
                .findByCredentialIdAndDeletedFalse(
                        credentialId)
                .orElse(null);

        if (credential == null) {
            return false;
        }

        return credential.getUser()
                .getUserId()
                .equals(user.getUserId());
    }

// @Override
// @Transactional(readOnly = true)
// public boolean canAccessCredential(
//         Credential credential,
//         User user) {

//     // Owner can always access their own credential
//     if (credential.getUser()
//             .getUserId()
//             .equals(user.getUserId())) {

//         return true;
//     }

//     // Otherwise check whether the credential is actively shared
//     CredentialShare share =
//             credentialShareRepository
//                     .findByCredentialCredentialIdAndSharedWithUserAndActiveTrue(
//                             credential.getCredentialId(),
//                             user)
//                     .orElse(null);

//     if (share == null) {
//         return false;
//     }

//     // Check share expiration
//     if (share.getExpiresAt() != null
//             && share.getExpiresAt()
//                     .isBefore(LocalDateTime.now())) {

//         return false;
//     }

//     return true;
// }


@Override
@Transactional(readOnly = true)
public boolean canAccessCredential(
        Credential credential,
        User user) {

    Long ownerId = credential.getUser().getUserId();
    Long currentUserId = user.getUserId();

    logger.info("========== CREDENTIAL ACCESS DEBUG ==========");
    logger.info("Credential ID: {}", credential.getCredentialId());
    logger.info("Credential Owner ID: {}", ownerId);
    logger.info("Current Logged-in User ID: {}", currentUserId);
    logger.info("Owner Email: {}", credential.getUser().getEmail());
    logger.info("Current User Email: {}", user.getEmail());

    // Owner can always access
    if (ownerId.equals(currentUserId)) {

        logger.info("ACCESS GRANTED: User is the owner");

        return true;
    }

    // Otherwise check whether credential is actively shared
    CredentialShare share =
            credentialShareRepository
                    .findByCredentialCredentialIdAndSharedWithUserAndActiveTrue(
                            credential.getCredentialId(),
                            user)
                    .orElse(null);

    if (share == null) {

        logger.warn("ACCESS DENIED: No active share found");

        return false;
    }

    if (share.getExpiresAt() != null
            && share.getExpiresAt().isBefore(LocalDateTime.now())) {

        logger.warn("ACCESS DENIED: Share has expired");

        return false;
    }

    logger.info("ACCESS GRANTED: Credential is shared");

    return true;
}


    @Override
    @Transactional(readOnly = true)
    public boolean canEditCredential(
            Long credentialId,
            User user) {

        if (isOwner(credentialId, user)) {
            return true;
        }

        CredentialShare share =
                credentialShareRepository
                        .findByCredentialCredentialIdAndSharedWithUserAndActiveTrue(
                                credentialId,
                                user)
                        .orElse(null);

        if (share == null) {
            return false;
        }

        if (share.getExpiresAt() != null
                && share.getExpiresAt()
                        .isBefore(LocalDateTime.now())) {

            return false;
        }

        return share.getPermission() == Permission.EDIT;
    }

    @Override
    @Transactional
    public void revokeShare(Long shareId) {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        User owner = (User) authentication.getPrincipal();

        CredentialShare share =
                credentialShareRepository
                        .findById(shareId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Share not found"));

        if (!share.getOwner()
                .getUserId()
                .equals(owner.getUserId())) {

            logger.warn(
                    "Unauthorized share revoke attempt: shareId={}, userId={}",
                    shareId,
                    owner.getUserId()
            );

            throw new AccessDeniedException(
                    "Only the owner can revoke sharing");
        }

        share.setActive(false);

        credentialShareRepository.save(share);

        logger.info(
                "Credential sharing revoked: shareId={}, credentialId={}, userId={}",
                shareId,
                share.getCredential().getCredentialId(),
                owner.getUserId()
        );
    }
}