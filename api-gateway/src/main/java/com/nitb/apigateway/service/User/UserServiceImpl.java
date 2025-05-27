package com.nitb.apigateway.service.User;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import com.nitb.apigateway.grpc.UserGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.UserMapper;
import com.nitb.common.grpc.ActionResponse;
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
    public Mono<ActionResponseDto> updateUser(UUID id, UpdateUserRequestDto request) {
        return Mono.fromCallable(()->{
            ActionResponse response = userGrpc.updateName(id, request.getName());
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> setUserLocked(UUID id, boolean isLocked) {
        return Mono.fromCallable(()->{
            ActionResponse response = userGrpc.setUserLocked(id, isLocked);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
