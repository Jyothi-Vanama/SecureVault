package com.securevault.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.securevault.service.AsyncTaskService;

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {

    @Override
    @Async("secureVaultTaskExecutor")
    public void sendEmailNotification() {

        System.out.println(
                "Email notification running on thread: "
                        + Thread.currentThread().getName()
        );
    }

    @Override
@Async("secureVaultTaskExecutor")
public void logActivity(Long credentialId, String action) {

    System.out.println(
            "Activity logging for credential " + credentialId
                    + " with action " + action
                    + " running on thread: "
                    + Thread.currentThread().getName()
    );
}

}