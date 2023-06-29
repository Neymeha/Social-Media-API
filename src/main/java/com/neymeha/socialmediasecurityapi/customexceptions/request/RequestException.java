package com.neymeha.socialmediasecurityapi.customexceptions.request;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class RequestException extends SocialMediaApiException {
    public RequestException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
