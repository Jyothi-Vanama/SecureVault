package com.securevault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.securevault.entity.Credential;
import com.securevault.entity.User;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    List<Credential> findByUser(User user);

}