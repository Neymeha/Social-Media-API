package com.neymeha.socialmediasecurityapi.customexceptions.request;

import org.springframework.http.HttpStatus;

public class RequiredFieldsAreNotFilledException extends RequestException{
    public RequiredFieldsAreNotFilledException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
