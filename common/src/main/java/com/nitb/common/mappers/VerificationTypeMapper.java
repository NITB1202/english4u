package com.nitb.common.mappers;

import com.nitb.common.enums.VerificationType;
import com.nitb.common.exceptions.BusinessException;

public class VerificationTypeMapper {
    private VerificationTypeMapper() {}

    public static VerificationType toEnum(com.nitb.authservice.grpc.VerificationType type) {
        return switch (type) {
            case REGISTER -> VerificationType.REGISTER;
            case RESET_PASSWORD -> VerificationType.RESET_PASSWORD;
            default -> throw new BusinessException("Invalid verification type");
        };
    }
}
