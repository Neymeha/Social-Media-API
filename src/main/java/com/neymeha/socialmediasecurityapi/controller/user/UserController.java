package com.neymeha.socialmediasecurityapi.controller.user;

import com.neymeha.socialmediasecurityapi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/media/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<UserResponse> getUser(){
        return new ResponseEntity<>(service.getUser(), HttpStatus.OK);
    }

    @GetMapping("/byId")
    public ResponseEntity<UserResponse> getUser(long userId){
        return new ResponseEntity<>(service.getUser(userId), HttpStatus.OK);
    }
}
