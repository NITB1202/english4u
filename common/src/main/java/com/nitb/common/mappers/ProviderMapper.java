package com.nitb.common.mappers;

import com.nitb.authservice.grpc.SupportedProvider;
import com.nitb.common.enums.Provider;
import com.nitb.common.exceptions.BusinessException;

public class ProviderMapper {
    private ProviderMapper() {}

    public static Provider toProvider(SupportedProvider supportedProvider) {
        return switch (supportedProvider) {
            case GOOGLE -> Provider.GOOGLE;
            case FACEBOOK -> Provider.FACEBOOK;
            default -> throw new BusinessException("Unsupported provider: " + supportedProvider);
        };
    }
}
