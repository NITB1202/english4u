package com.nitb.common.mappers;

import com.nitb.authservice.grpc.SupportedRole;
import com.nitb.common.enums.UserRole;
import com.nitb.common.exceptions.BusinessException;

public class UserRoleMapper {
    private UserRoleMapper() {}

    public static UserRole toUserRole(SupportedRole role) {
        return switch (role) {
            case ADMIN -> UserRole.ADMIN;
            case LEARNER -> UserRole.LEARNER;
            default -> throw new BusinessException("Unsupported role: " + role);
        };
    }

    public static SupportedRole toSupportedRole(UserRole role) {
        return switch (role) {
            case ADMIN -> SupportedRole.ADMIN;
            case LEARNER -> SupportedRole.LEARNER;
            default -> throw new BusinessException("Unsupported role: " + role);
        };
    }

    public static UserRole fromString(String role) {
        return switch (role) {
            case "SYSTEM_ADMIN" -> UserRole.SYSTEM_ADMIN;
            case "ADMIN" -> UserRole.ADMIN;
            case "LEARNER" -> UserRole.LEARNER;
            default -> throw new BusinessException("Unsupported role: " + role);
        };
    }
}
