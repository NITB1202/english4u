package com.nitb.userservice.mapper;

import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.UserDetailResponse;
import com.nitb.userservice.grpc.UserResponse;
import com.nitb.userservice.grpc.UsersResponse;

import java.util.List;

public class UserMapper {
    private UserMapper() {}

    public static UserResponse toUserResponse(User user) {
        return UserResponse.newBuilder()
                .setId(user.getId().toString())
                .setName(user.getName())
                .setAvatarUrl(user.getAvatarUrl())
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
}
