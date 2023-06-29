package com.neymeha.socialmediasecurityapi.controller.feed;

import com.neymeha.socialmediasecurityapi.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {
    private List<Post> posts;
    private int pageNo;
    private int pageSize;
    private int tatalPages;
}
