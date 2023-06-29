package com.neymeha.socialmediasecurityapi.controller.exception;

import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler
    public ResponseException handle(SocialMediaApiException exception){
        log.error(exception.getMessage(), exception);
        return new ResponseException(exception);
    }

    @ExceptionHandler
    public ResponseException handle(Exception exception){
        log.error(exception.getMessage(), exception);
        return new ResponseException(exception.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }
}
