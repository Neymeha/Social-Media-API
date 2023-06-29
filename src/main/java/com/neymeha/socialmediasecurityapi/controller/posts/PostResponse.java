package com.neymeha.socialmediasecurityapi.controller.posts;

import com.neymeha.socialmediasecurityapi.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Post post;
}


