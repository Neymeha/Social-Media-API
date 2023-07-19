package com.neymeha.socialmediasecurityapi.controller.status;

import com.neymeha.socialmediasecurityapi.service.status.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/media/api/v1/status")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService service;

    @PostMapping("/friends")
    public ResponseEntity<StatusResponse> requestForFriend(@RequestBody StatusRequest request){
        return new ResponseEntity(service.friendAddRequest(request), HttpStatus.CREATED);
    }

    @PutMapping("/friends")
    public ResponseEntity<StatusResponse> confirmFriend(@RequestBody StatusRequest request){
        return new ResponseEntity(service.friendAddConfirm(request), HttpStatus.ACCEPTED);
    }

    @GetMapping("/friends")
    public ResponseEntity<StatusResponse> refuseFriend(@RequestBody StatusRequest request){
        return new ResponseEntity(service.friendAddRefuse(request), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/friends")
    public ResponseEntity<StatusResponse> deleteFriend(@RequestBody StatusRequest request){
        return new ResponseEntity(service.friendDelete(request), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/subscriptions")
    public ResponseEntity<StatusResponse> deleteSubscription(@RequestBody StatusRequest request){
        return ResponseEntity.ok(service.subscriptionDelete(request));
    }
}
