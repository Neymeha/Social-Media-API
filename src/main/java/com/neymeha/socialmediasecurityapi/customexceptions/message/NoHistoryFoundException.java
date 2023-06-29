package com.neymeha.socialmediasecurityapi.customexceptions.message;

import org.springframework.http.HttpStatus;

public class NoHistoryFoundException extends MessageException{
    public NoHistoryFoundException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
