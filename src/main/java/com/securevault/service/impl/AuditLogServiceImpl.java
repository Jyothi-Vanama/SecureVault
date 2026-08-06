package com.securevault.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.securevault.entity.AuditLog;
import com.securevault.entity.User;
import com.securevault.repository.AuditLogRepository;
import com.securevault.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void saveAuditLog(
            String action,
            String entityType,
            Long entityId,
            User user) {

        AuditLog auditLog = new AuditLog();

        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setPerformedBy(user.getEmail());
        auditLog.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(auditLog);
        
    }
}