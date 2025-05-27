package com.nitb.userservice.service.impl;

import com.nitb.common.exceptions.BusinessException;
import com.nitb.common.exceptions.NotFoundException;
import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.*;
import com.nitb.userservice.repository.UserRepository;
import com.nitb.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void checkCanPerformAction(CheckCanPerformActionRequest request) {
        UUID userId = UUID.fromString(request.getUserId());

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );

        if(user.isLocked()) {
            throw new BusinessException("This user is locked.");
        }
    }

    @Override
    public void createUser(CreateUserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .joinAt(LocalDateTime.now())
                .isLocked(false)
                .build();

        userRepository.save(user);
    }

    @Override
    public User getUserById(GetUserByIdRequest request) {
        UUID userId = UUID.fromString(request.getId());
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );
    }

    @Override
    public List<User> getUsersByListOfIds(GetUsersByListOfIdsRequest request) {
        List<UUID> ids = request.getIdsList().stream()
                .map(UUID::fromString)
                .toList();

        List<User> users = userRepository.findAllById(ids);
        Map<UUID, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // Return users in the same order as the input ID list
        return ids.stream()
                .map(userMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void updateName(UpdateNameRequest request) {
        UUID userId = UUID.fromString(request.getId());

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );

        user.setName(request.getName());
        userRepository.save(user);
    }

    @Override
    public void updateAvatar(UpdateAvatarRequest request) {
        UUID userId = UUID.fromString(request.getId());

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );

        user.setAvatarUrl(request.getAvatarUrl());
        userRepository.save(user);
    }

    @Override
    public void setUserLocked(SetUserLockedRequest request) {
        UUID userId = UUID.fromString(request.getId());

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id " + userId + " not found")
        );

        user.setLocked(request.getIsLocked());
        userRepository.save(user);
    }
}
