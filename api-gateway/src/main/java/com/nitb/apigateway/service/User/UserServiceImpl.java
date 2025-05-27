package com.nitb.apigateway.service.User;

import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UpdateUserResponseDto;
import com.nitb.apigateway.dto.User.response.UserLockedResponseDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import com.nitb.apigateway.grpc.UserGrpcClient;
import com.nitb.apigateway.mapper.UserMapper;
import com.nitb.userservice.grpc.UpdateUserResponse;
import com.nitb.userservice.grpc.UserLockedResponse;
import com.nitb.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserGrpcClient userGrpc;

    @Override
    public Mono<UserResponseDto> getUserById(UUID id) {
        return Mono.fromCallable(()->{
            UserResponse response = userGrpc.getUserById(id);
            return UserMapper.toUserResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UpdateUserResponseDto> updateUser(UUID id, UpdateUserRequestDto request) {
        return Mono.fromCallable(()-> {
            UpdateUserResponse response = userGrpc.updateUser(id, request);
            return UserMapper.toUpdateUserResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserLockedResponseDto> setUserLocked(UUID id, boolean isLocked) {
        return Mono.fromCallable(()->{
            UserLockedResponse response = userGrpc.setUserLocked(id, isLocked);
            return UserMapper.toUserLockedResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
