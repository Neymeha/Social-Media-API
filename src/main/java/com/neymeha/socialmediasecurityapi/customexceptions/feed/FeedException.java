package com.neymeha.socialmediasecurityapi.customexceptions.feed;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class FeedException extends SocialMediaApiException {
    public FeedException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
