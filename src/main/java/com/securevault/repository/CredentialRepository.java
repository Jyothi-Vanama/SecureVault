package com.securevault.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.securevault.entity.Category;
import com.securevault.entity.Credential;
import com.securevault.entity.User;

public interface CredentialRepository
        extends JpaRepository<Credential, Long>,
                JpaSpecificationExecutor<Credential> {

    // Get all credentials of the logged-in user
    Page<Credential> findByUser(User user, Pageable pageable);

    // Search by title, username or website for the logged-in user
    @Query("""
            SELECT c
            FROM Credential c
            WHERE c.user = :user
            AND (
                LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.username) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(c.website) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    List<Credential> searchCredentials(
            @Param("user") User user,
            @Param("keyword") String keyword);

    // Filter credentials by category for the logged-in user
    Page<Credential> findByUserAndCategory(
            User user,
            Category category,
            Pageable pageable);
}