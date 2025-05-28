package com.nitb.apigateway.grpc;

import com.google.protobuf.Empty;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.userservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceGrpcClient {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public Empty checkCanPerformAction(UUID userId) {
        CheckCanPerformActionRequest request = CheckCanPerformActionRequest.newBuilder()
                .setUserId(userId.toString())
                .build();

        return blockingStub.checkCanPerformAction(request);
    }

    public Empty createUser(String name) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setName(name)
                .build();

        return blockingStub.createUser(request);
    }

    public UserResponse getUserById(UUID id) {
        GetUserByIdRequest request = GetUserByIdRequest.newBuilder()
                .setId(id.toString())
                .build();

        return blockingStub.getUserById(request);
    }

    public UsersResponse getUsersByListOfIds(List<UUID> ids) {
        List<String> idsStr = ids.stream()
                .map(UUID::toString)
                .toList();

        GetUsersByListOfIdsRequest request = GetUsersByListOfIdsRequest.newBuilder()
                .addAllIds(idsStr)
                .build();

        return blockingStub.getUsersByListOfIds(request);
    }

    public UpdateUserResponse updateUser(UUID id, UpdateUserRequestDto dto) {
        UpdateUserRequest request = UpdateUserRequest.newBuilder()
                .setId(id.toString())
                .setName(dto.getName())
                .build();

        return blockingStub.updateUser(request);
    }

    public ActionResponse updateAvatar(UUID id, String avatarUrl) {
        UpdateAvatarRequest request = UpdateAvatarRequest.newBuilder()
                .setId(id.toString())
                .setAvatarUrl(avatarUrl)
                .build();

        return blockingStub.updateAvatar(request);
    }

    public UserLockedResponse setUserLocked(UUID id, boolean isLocked) {
        SetUserLockedRequest request = SetUserLockedRequest.newBuilder()
                .setId(id.toString())
                .setIsLocked(isLocked)
                .build();

        return blockingStub.setUserLocked(request);
    }
}
