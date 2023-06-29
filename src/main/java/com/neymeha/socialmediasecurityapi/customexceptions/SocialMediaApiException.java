package com.neymeha.socialmediasecurityapi.customexceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class SocialMediaApiException extends RuntimeException {
    private final HttpStatus status;
    public SocialMediaApiException(String reason, HttpStatus status){
        super(reason);
        this.status = status;
    }
}
