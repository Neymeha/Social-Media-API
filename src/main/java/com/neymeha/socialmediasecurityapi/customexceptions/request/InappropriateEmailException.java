package com.neymeha.socialmediasecurityapi.customexceptions.request;

import org.springframework.http.HttpStatus;

public class InappropriateEmailException extends RequestException{
    public InappropriateEmailException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
