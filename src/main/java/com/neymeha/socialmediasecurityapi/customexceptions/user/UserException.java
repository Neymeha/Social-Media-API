package com.neymeha.socialmediasecurityapi.customexceptions.user;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class UserException extends SocialMediaApiException {
    public UserException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
