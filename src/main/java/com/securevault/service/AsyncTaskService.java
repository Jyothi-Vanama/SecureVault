package com.securevault.service;

public interface AsyncTaskService {

    void sendEmailNotification();

    void logActivity(Long credentialId, String action);

}