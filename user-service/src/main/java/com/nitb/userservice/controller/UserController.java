package com.nitb.userservice.controller;

import com.google.protobuf.Empty;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.*;
import com.nitb.userservice.mapper.UserMapper;
import com.nitb.userservice.service.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class UserController extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

    @Override
    public void checkCanPerformAction(CheckCanPerformActionRequest request, StreamObserver<Empty> responseObserver) {
        userService.checkCanPerformAction(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<Empty> responseObserver) {
        userService.createUser(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = userService.getUserById(request);
        UserResponse response = UserMapper.toUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersByListOfIds(GetUsersByListOfIdsRequest request, StreamObserver<UsersResponse> responseObserver) {
        List<User> users = userService.getUsersByListOfIds(request);
        UsersResponse response = UserMapper.toUsersResponse(users);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UpdateUserResponse> responseObserver) {
        User user = userService.updateUser(request);
        UpdateUserResponse response = UserMapper.toUpdateUserResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateAvatar(UpdateAvatarRequest request, StreamObserver<ActionResponse> responseObserver) {
        String avatarUrl = userService.updateAvatar(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage(avatarUrl)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void setUserLocked(SetUserLockedRequest request, StreamObserver<UserLockedResponse> responseObserver) {
        User user = userService.setUserLocked(request);
        UserLockedResponse response = UserMapper.toUserLockedResponse(user);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
