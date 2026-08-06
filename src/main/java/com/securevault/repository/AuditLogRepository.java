package com.securevault.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.securevault.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

}