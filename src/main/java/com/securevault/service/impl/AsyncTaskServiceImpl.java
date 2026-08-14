package com.securevault.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.securevault.service.AsyncTaskService;

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {

    private static final Logger logger =
            LoggerFactory.getLogger(AsyncTaskServiceImpl.class);

    @Override
    @Async("secureVaultTaskExecutor")
    public void sendEmailNotification() {

        logger.info(
                "Email notification running on thread: {}",
                Thread.currentThread().getName()
        );
    }

    @Override
    @Async("secureVaultTaskExecutor")
    public void logActivity(Long credentialId, String action) {

        logger.info(
                "Activity logging for credential {} with action {} running on thread: {}",
                credentialId,
                action,
                Thread.currentThread().getName()
        );
    }
}