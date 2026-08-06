package com.securevault.service;

import com.securevault.entity.User;

public interface AuditLogService {

    void saveAuditLog(
            String action,
            String entityType,
            Long entityId,
            User user
    );

}