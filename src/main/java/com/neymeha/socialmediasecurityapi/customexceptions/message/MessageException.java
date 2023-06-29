package com.neymeha.socialmediasecurityapi.customexceptions.message;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import org.springframework.http.HttpStatus;

public class MessageException extends SocialMediaApiException {
    public MessageException(String reason, HttpStatus status) {
        super(reason, status);
    }
}
