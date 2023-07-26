package com.neymeha.socialmediasecurityapi.service.user;

import com.neymeha.socialmediasecurityapi.controller.user.UserResponse;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.entity.User;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public UserResponse getUser() {
        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .userIdRequestedForFriendship(user.getUserIdRequestedForFriendship())
                .userIdThatSendAMessage(user.getUserIdThatSendAMessage())
                .build();
    }

    @Override
    public UserResponse getUser(long userId) {
        if (userId<1) {
            throw new RequestException("User id should not be less than 1", HttpStatus.BAD_REQUEST);
        }
        var user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .userIdRequestedForFriendship(user.getUserIdRequestedForFriendship())
                .userIdThatSendAMessage(user.getUserIdThatSendAMessage())
                .build();
    }
}
