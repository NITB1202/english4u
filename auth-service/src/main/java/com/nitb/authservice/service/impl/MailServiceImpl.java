package com.nitb.authservice.service.impl;

import com.nitb.authservice.grpc.SupportedRole;
import com.nitb.authservice.service.MailService;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    @Override
    public void sendVerificationEmail(String email, String verificationCode) {

    }

    @Override
    public void sendAdminAccountEmail(String email, String password) {

    }

    @Override
    public void sendUpdateRoleEmail(String email, SupportedRole role) {

    }
}
