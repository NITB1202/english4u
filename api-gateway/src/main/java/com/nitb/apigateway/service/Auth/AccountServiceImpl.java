package com.nitb.apigateway.service.Auth;

import com.nitb.apigateway.dto.Auth.Account.request.CreateAdminAccountRequestDto;
import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.grpc.AuthServiceGrpcClient;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.common.enums.UserRole;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.userservice.grpc.CreateUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AuthServiceGrpcClient authGrpc;
    private final UserServiceGrpcClient userGrpc;

    @Override
    public Mono<ActionResponseDto> createAdminAccount(CreateAdminAccountRequestDto request) {
        return Mono.fromCallable(()->{
            CreateUserResponse user = userGrpc.createUser(request.getName(), null);
            UUID userId = UUID.fromString(user.getUserId());
            ActionResponse response = authGrpc.createAdminAccount(userId, request);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<ActionResponseDto> updateRole(UUID id, UserRole role) {
        return Mono.fromCallable(()-> {
            ActionResponse response = authGrpc.updateRole(id, role);
            return ActionMapper.toResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
