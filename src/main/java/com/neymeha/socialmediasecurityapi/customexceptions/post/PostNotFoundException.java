package com.neymeha.socialmediasecurityapi.customexceptions.post;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends PostException{
    public PostNotFoundException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
