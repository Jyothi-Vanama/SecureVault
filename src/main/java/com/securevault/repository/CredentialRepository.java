package com.securevault.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.securevault.entity.Credential;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

}