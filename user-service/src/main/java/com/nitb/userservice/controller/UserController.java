package com.nitb.userservice.controller;

import com.google.protobuf.Empty;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.*;
import com.nitb.userservice.mapper.UserMapper;
import com.nitb.userservice.service.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

    @Override
    public void checkCanPerformAction(CheckCanPerformActionRequest request, StreamObserver<Empty> responseObserver) {
        userService.checkCanPerformAction(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<ActionResponse> responseObserver) {
        userService.createUser(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Create successfully.")
                .build();

        responseObserver.onNext(response);
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
    public void updateName(UpdateNameRequest request, StreamObserver<ActionResponse> responseObserver) {
        userService.updateName(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateAvatar(UpdateAvatarRequest request, StreamObserver<ActionResponse> responseObserver) {
        userService.updateAvatar(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Update successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void setUserLocked(SetUserLockedRequest request, StreamObserver<ActionResponse> responseObserver) {
        userService.setUserLocked(request);

        ActionResponse response = ActionResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Set successfully.")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
