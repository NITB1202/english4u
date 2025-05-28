package com.nitb.userservice.service;

import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.*;

import java.util.List;

public interface UserService {
    void checkCanPerformAction(CheckCanPerformActionRequest request);
    void createUser(CreateUserRequest request);
    User getUserById(GetUserByIdRequest request);
    List<User> getUsersByListOfIds(GetUsersByListOfIdsRequest request);
    User updateUser(UpdateUserRequest request);
    String updateAvatar(UpdateAvatarRequest request);
    User setUserLocked(SetUserLockedRequest request);
}
