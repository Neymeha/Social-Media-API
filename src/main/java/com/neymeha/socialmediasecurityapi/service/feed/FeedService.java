package com.neymeha.socialmediasecurityapi.service.feed;

import com.neymeha.socialmediasecurityapi.controller.feed.FeedRequest;
import com.neymeha.socialmediasecurityapi.controller.feed.FeedResponse;

public interface FeedService {
    public FeedResponse showFeed(FeedRequest request);
}
