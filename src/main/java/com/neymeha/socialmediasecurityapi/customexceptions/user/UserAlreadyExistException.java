package com.neymeha.socialmediasecurityapi.customexceptions.user;


import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends UserException{
    public UserAlreadyExistException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
