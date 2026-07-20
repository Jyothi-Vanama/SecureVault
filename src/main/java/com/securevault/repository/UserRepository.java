package com.securevault.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.securevault.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

}