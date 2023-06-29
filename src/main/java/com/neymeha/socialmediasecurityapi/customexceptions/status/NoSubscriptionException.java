package com.neymeha.socialmediasecurityapi.customexceptions.status;

import org.springframework.http.HttpStatus;

public class NoSubscriptionException extends StatusException{
    public NoSubscriptionException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
