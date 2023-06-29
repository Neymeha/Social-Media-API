package com.neymeha.socialmediasecurityapi.controller.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedRequest {
    int pageNo;
    int pageSize;
}
