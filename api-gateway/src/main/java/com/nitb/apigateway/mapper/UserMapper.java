package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.User.response.UpdateUserResponseDto;
import com.nitb.apigateway.dto.User.response.UserLockedResponseDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import com.nitb.userservice.grpc.UpdateUserResponse;
import com.nitb.userservice.grpc.UserLockedResponse;
import com.nitb.userservice.grpc.UserResponse;

import java.util.UUID;

public class UserMapper {
    private UserMapper() {}

    public static UserResponseDto toUserResponseDto(UserResponse user) {
        return UserResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public static UpdateUserResponseDto toUpdateUserResponseDto(UpdateUserResponse user) {
        return UpdateUserResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .name(user.getName())
                .build();
    }

    public static UserLockedResponseDto toUserLockedResponseDto(UserLockedResponse user) {
        return UserLockedResponseDto.builder()
                .id(UUID.fromString(user.getId()))
                .name(user.getName())
                .isLocked(user.getIsLocked())
                .build();
    }
}
