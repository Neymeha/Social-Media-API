package com.neymeha.socialmediasecurityapi.controller.user;

import com.neymeha.socialmediasecurityapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social/media/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(){
        return new ResponseEntity<>(service.getUser(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(long userId){
        return new ResponseEntity<>(service.getUser(userId), HttpStatus.OK);
    }
}
