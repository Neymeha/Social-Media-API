package com.neymeha.socialmediasecurityapi.customexceptions.user;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends UserException{
    public UserNotFoundException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
