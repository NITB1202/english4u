package com.nitb.apigateway.service.Auth;

import com.nitb.apigateway.dto.Auth.Account.request.CreateAdminAccountRequestDto;
import com.nitb.apigateway.dto.Auth.Auth.request.*;
import com.nitb.apigateway.dto.Auth.Auth.response.GenerateAccessTokenResponseDto;
import com.nitb.apigateway.dto.Auth.Auth.response.LoginResponseDto;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.common.enums.UserRole;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthService {
    Mono<LoginResponseDto> loginWithCredentials(LoginWithCredentialsRequestDto request);
    Mono<LoginResponseDto> loginWithProvider(LoginWithProviderRequestDto request);
    Mono<ActionResponseDto> validateRegisterInfo(ValidateRegisterInfoRequestDto request);
    Mono<ActionResponseDto> registerWithCredentials(RegisterWithCredentialsRequestDto request);
    Mono<ActionResponseDto> sendResetPasswordCode(String email);
    Mono<ActionResponseDto> resetPassword(ResetPasswordRequestDto request);
    Mono<ActionResponseDto> createAdminAccount(CreateAdminAccountRequestDto request);
    Mono<ActionResponseDto> updateRole(UUID id, UserRole role);
    Mono<GenerateAccessTokenResponseDto> generateAccessToken(String refreshToken);
}
