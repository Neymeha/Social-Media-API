package com.neymeha.socialmediasecurityapi.controller.message;


import com.neymeha.socialmediasecurityapi.controller.feed.FeedRequest;
import com.neymeha.socialmediasecurityapi.controller.feed.FeedResponse;
import com.neymeha.socialmediasecurityapi.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/media/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request){
        return new ResponseEntity(service.sendAndSaveMessage(request), HttpStatus.OK);
    }
}
