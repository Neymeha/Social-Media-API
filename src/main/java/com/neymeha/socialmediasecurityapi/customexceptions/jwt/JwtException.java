package com.neymeha.socialmediasecurityapi.customexceptions.jwt;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class JwtException extends SocialMediaApiException {
    public JwtException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
