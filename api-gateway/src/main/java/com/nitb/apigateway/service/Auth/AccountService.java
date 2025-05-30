package com.nitb.apigateway.service.Auth;

import com.nitb.apigateway.dto.Auth.Account.request.CreateAdminAccountRequestDto;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.common.enums.UserRole;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountService {
    Mono<ActionResponseDto> createAdminAccount(CreateAdminAccountRequestDto request);
    Mono<ActionResponseDto> updateRole(UUID id, UserRole role);
}
