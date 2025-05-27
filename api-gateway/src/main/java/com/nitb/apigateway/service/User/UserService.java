package com.nitb.apigateway.service.User;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserResponseDto> getUserById(UUID id);
    Mono<ActionResponseDto> updateUser(UUID id, UpdateUserRequestDto request);
    Mono<ActionResponseDto> setUserLocked(UUID id, boolean isLocked);
}
