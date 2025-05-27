package com.nitb.userservice.service;

import com.nitb.userservice.entity.User;
import com.nitb.userservice.grpc.*;

import java.util.List;

public interface UserService {
    void checkCanPerformAction(CheckCanPerformActionRequest request);
    void createUser(CreateUserRequest request);
    User getUserById(GetUserByIdRequest request);
    List<User> getUsersByListOfIds(GetUsersByListOfIdsRequest request);
    void updateName(UpdateNameRequest request);
    void updateAvatar(UpdateAvatarRequest request);
    void setUserLocked(SetUserLockedRequest request);
}
