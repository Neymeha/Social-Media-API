package com.neymeha.socialmediasecurityapi.controller.user;

import com.neymeha.socialmediasecurityapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long userId;
    private String name;
    private List<Long> userIdRequestedForFriendship;
    private List<Long> userIdThatSendAMessage;
}
