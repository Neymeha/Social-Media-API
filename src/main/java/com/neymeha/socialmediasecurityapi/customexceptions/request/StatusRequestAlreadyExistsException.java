package com.neymeha.socialmediasecurityapi.customexceptions.request;

import org.springframework.http.HttpStatus;

public class StatusRequestAlreadyExistsException extends RequestException{
    public StatusRequestAlreadyExistsException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
