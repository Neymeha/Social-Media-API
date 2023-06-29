package com.neymeha.socialmediasecurityapi.controller.auth;

import com.neymeha.socialmediasecurityapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// класс для ответов по аутентификации
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String jwtAccessToken;
    private String jwtRefreshToken;
}
