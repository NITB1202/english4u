package com.nitb.authservice.service;

public interface MailService {
    void sendVerificationEmail(String email, String verificationCode);
    void sendAdminAccountEmail(String email, String password);
    void sendUpdateRoleEmail(String email, String oldRole, String newRole);
}
