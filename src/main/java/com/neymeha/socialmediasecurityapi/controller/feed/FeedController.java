package com.neymeha.socialmediasecurityapi.controller.feed;

import com.neymeha.socialmediasecurityapi.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/media/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService service;
    @GetMapping("/subscriptionsFeed")
    public ResponseEntity<FeedResponse> getChatHistory(@RequestBody FeedRequest request){
        return new ResponseEntity(service.showFeed(request), HttpStatus.OK);
    }
}
