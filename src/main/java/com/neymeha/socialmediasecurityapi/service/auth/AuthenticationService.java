package com.neymeha.socialmediasecurityapi.service.auth;

import com.neymeha.socialmediasecurityapi.controller.auth.AuthenticationRequest;
import com.neymeha.socialmediasecurityapi.controller.auth.AuthenticationResponse;
import com.neymeha.socialmediasecurityapi.controller.auth.RefreshRequest;
import com.neymeha.socialmediasecurityapi.controller.auth.RegisterRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(RefreshRequest refreshRequest);
}
