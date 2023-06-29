package com.neymeha.socialmediasecurityapi.controller.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ResponseException {

    public final HttpStatus status;
    public final String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.now();

    public ResponseException (SocialMediaApiException exception){
        this.message = exception.getMessage();
        this.status = exception.getStatus();
    }

    public ResponseException(String message, HttpStatus status){
        this.message = message;
        this.status = status;
    }

}
