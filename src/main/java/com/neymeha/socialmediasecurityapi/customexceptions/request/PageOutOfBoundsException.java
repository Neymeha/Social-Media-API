package com.neymeha.socialmediasecurityapi.customexceptions.request;

import org.springframework.http.HttpStatus;

public class PageOutOfBoundsException extends RequestException{
    public PageOutOfBoundsException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
