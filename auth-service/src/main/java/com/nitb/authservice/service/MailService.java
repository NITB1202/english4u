package com.nitb.authservice.service;

import com.nitb.authservice.grpc.SupportedRole;

public interface MailService {
    void sendVerificationEmail(String email, String verificationCode);
    void sendAdminAccountEmail(String email, String password);
    void sendUpdateRoleEmail(String email, SupportedRole role);
}
