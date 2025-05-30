package com.nitb.apigateway.service.Auth;

import com.nitb.apigateway.dto.Auth.Auth.request.*;
import com.nitb.apigateway.dto.Auth.Auth.response.GenerateAccessTokenResponseDto;
import com.nitb.apigateway.dto.Auth.Auth.response.LoginResponseDto;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LoginResponseDto> loginWithCredentials(LoginWithCredentialsRequestDto request);
    Mono<LoginResponseDto> loginWithProvider(LoginWithProviderRequestDto request);
    Mono<ActionResponseDto> validateRegisterInfo(ValidateRegisterInfoRequestDto request);
    Mono<ActionResponseDto> registerWithCredentials(RegisterWithCredentialsRequestDto request);
    Mono<ActionResponseDto> sendResetPasswordCode(String email);
    Mono<ActionResponseDto> resetPassword(ResetPasswordRequestDto request);
    Mono<GenerateAccessTokenResponseDto> generateAccessToken(String refreshToken);
}
