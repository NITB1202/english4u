package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.Auth.Auth.response.GenerateAccessTokenResponseDto;
import com.nitb.apigateway.dto.Auth.Auth.response.LoginResponseDto;
import com.nitb.authservice.grpc.LoginResponse;

public class AuthMapper {
    private AuthMapper() {}

    public static LoginResponseDto toLoginResponseDto(LoginResponse login) {
        return LoginResponseDto.builder()
                .accessToken(login.getAccessToken())
                .refreshToken(login.getRefreshToken())
                .build();
    }

    public static GenerateAccessTokenResponseDto toGenerateAccessTokenResponseDto(String accessToken) {
        return GenerateAccessTokenResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }
}
