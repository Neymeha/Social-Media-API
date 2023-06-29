package com.neymeha.socialmediasecurityapi.customexceptions.post;

import org.springframework.http.HttpStatus;

public class NoPostsUnderSubscriptionsException extends PostException{
    public NoPostsUnderSubscriptionsException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
