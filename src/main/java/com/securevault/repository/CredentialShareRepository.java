package com.securevault.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.securevault.entity.CredentialShare;
import com.securevault.entity.User;
import org.springframework.data.repository.query.Param;


public interface CredentialShareRepository
        extends JpaRepository<CredentialShare, Long> {

    Optional<CredentialShare> findByCredentialCredentialIdAndSharedWithUserAndActiveTrue(
            Long credentialId,
            User sharedWithUser);

    @Query("""
    SELECT cs
    FROM CredentialShare cs
    JOIN FETCH cs.credential c
    WHERE cs.sharedWithUser = :sharedWithUser
    AND cs.active = true
    AND c.deleted = false
""")
List<CredentialShare> findBySharedWithUserAndActiveTrue(
        @Param("sharedWithUser") User sharedWithUser);

    boolean existsByCredentialCredentialIdAndSharedWithUserAndActiveTrue(
            Long credentialId,
            User sharedWithUser);
}