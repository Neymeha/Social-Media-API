package com.neymeha.socialmediasecurityapi.customexceptions.status;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class StatusException extends SocialMediaApiException {
    public StatusException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
