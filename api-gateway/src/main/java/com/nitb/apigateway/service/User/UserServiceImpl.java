package com.nitb.apigateway.service.User;

import com.nitb.apigateway.dto.General.ActionResponseDto;
import com.nitb.apigateway.dto.User.request.UpdateUserRequestDto;
import com.nitb.apigateway.dto.User.response.UpdateUserResponseDto;
import com.nitb.apigateway.dto.User.response.UserLockedResponseDto;
import com.nitb.apigateway.dto.User.response.UserResponseDto;
import com.nitb.apigateway.grpc.FileServiceGrpcClient;
import com.nitb.apigateway.grpc.UserServiceGrpcClient;
import com.nitb.apigateway.mapper.ActionMapper;
import com.nitb.apigateway.mapper.UserMapper;
import com.nitb.apigateway.util.FileUtils;
import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.grpc.ActionResponse;
import com.nitb.fileservice.grpc.FileResponse;
import com.nitb.userservice.grpc.UpdateUserResponse;
import com.nitb.userservice.grpc.UserLockedResponse;
import com.nitb.userservice.grpc.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserServiceGrpcClient userGrpc;
    private final FileServiceGrpcClient fileGrpc;
    private final String AVATAR_FOLDER = "avatars";

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

    @Override
    public Mono<ActionResponseDto> uploadUserAvatar(UUID id, FilePart file) {
        if(!FileUtils.isImage(file)) {
            throw new BusinessException("User's avatar must be an image.");
        }

        return DataBufferUtils.join(file.content())
                .flatMap(buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    FileResponse avatar = fileGrpc.uploadFile(AVATAR_FOLDER, id.toString(), bytes);
                    ActionResponse response = userGrpc.updateAvatar(id, avatar.getUrl());

                    return Mono.fromCallable(() -> ActionMapper.toResponseDto(response))
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }
}
