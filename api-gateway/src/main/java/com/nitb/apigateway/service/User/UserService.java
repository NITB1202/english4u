package com.nitb.apigateway.service.User;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UpdateUserResponseDto;
import com.nitb.apigateway.dto.User.response.UserLockedResponseDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserResponseDto> getUserById(UUID id);
    Mono<UpdateUserResponseDto> updateUser(UUID id, UpdateUserRequestDto request);
    Mono<UserLockedResponseDto> setUserLocked(UUID id, boolean isLocked);
    Mono<ActionResponseDto> uploadUserAvatar(UUID id, FilePart file);
}
