package com.nitb.apigateway.mapper;

import com.nitb.apigateway.dto.User.response.UserResponseDto;
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
}
