package com.nitb.authservice.service.impl;

import com.nitb.authservice.grpc.VerificationType;
import com.nitb.authservice.service.CodeService;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeServiceImpl implements CodeService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generateCode(String email, VerificationType type) {
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public boolean verifyCode(String email, String code, VerificationType type) {
        return false;
    }
}
