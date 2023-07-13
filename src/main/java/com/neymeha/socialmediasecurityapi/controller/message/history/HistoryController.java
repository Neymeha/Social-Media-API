package com.neymeha.socialmediasecurityapi.controller.message.history;

import com.neymeha.socialmediasecurityapi.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/media/api/v1/messages/history")
@RequiredArgsConstructor
public class HistoryController {
    private final MessageService service;
    @GetMapping("/withFriend")
    public ResponseEntity<HistoryResponse> getChatHistory(@RequestBody HistoryLoadRequest request){
        return new ResponseEntity(service.getHistoryWithFriend(request), HttpStatus.OK);
    }
}
