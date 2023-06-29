package com.neymeha.socialmediasecurityapi.controller.posts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private String title;
    private String body;
    private String image;
}
