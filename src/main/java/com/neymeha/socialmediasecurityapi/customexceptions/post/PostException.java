package com.neymeha.socialmediasecurityapi.customexceptions.post;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class PostException extends SocialMediaApiException {
    public PostException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
