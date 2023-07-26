package com.neymeha.socialmediasecurityapi.service.user;

import com.neymeha.socialmediasecurityapi.controller.user.UserResponse;

public interface UserService {
    UserResponse getUser();
    UserResponse getUser(long userId);

}
