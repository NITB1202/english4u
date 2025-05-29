package com.nitb.authservice.service;

import com.nitb.authservice.grpc.VerificationType;

public interface CodeService {
    String generateCode(String email, VerificationType type);
    boolean verifyCode(String email, String code, VerificationType type);
}
