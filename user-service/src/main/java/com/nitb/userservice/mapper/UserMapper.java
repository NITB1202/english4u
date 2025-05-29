package com.nitb.userservice.mapper;

import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.*;

import java.util.List;

public class UserMapper {
    private UserMapper() {}

    public static CreateUserResponse toCreateUserResponse(User user) {
        return CreateUserResponse.newBuilder()
                .setUserId(user.getId().toString())
                .build();
    }

    public static UserResponse toUserResponse(User user) {
        return UserResponse.newBuilder()
                .setId(user.getId().toString())
                .setName(user.getName())
                .setAvatarUrl(user.getAvatarUrl())
                .setIsLocked(user.isLocked())
                .build();
    }

    public static UserDetailResponse toUserDetailResponse(User user) {
        return UserDetailResponse.newBuilder()
                .setId(user.getId().toString())
                .setName(user.getName())
                .setJoinAt(user.getJoinAt().toString())
                .setIsLocked(user.isLocked())
                .build();
    }

    public static UsersResponse toUsersResponse(List<User> users) {
        List<UserDetailResponse> responses = users.stream()
                .map(UserMapper::toUserDetailResponse)
                .toList();

        return UsersResponse.newBuilder()
                .addAllUsers(responses)
                .build();
    }

    public static UpdateUserResponse toUpdateUserResponse(User user) {
        return UpdateUserResponse.newBuilder()
                .setId(user.getId().toString())
                .setName(user.getName())
                .build();
    }

    public static UserLockedResponse toUserLockedResponse(User user) {
        return UserLockedResponse.newBuilder()
                .setId(user.getId().toString())
                .setName(user.getName())
                .setIsLocked(user.isLocked())
                .build();
    }
}
