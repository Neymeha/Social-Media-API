package com.neymeha.socialmediasecurityapi.service.post;

import com.neymeha.socialmediasecurityapi.controller.posts.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {
    PostResponse createPost(String title, String body, MultipartFile image);
    PostResponse deletePost(long postId);
    PostResponse updatePost(long postId, String title, String body, MultipartFile image);
    PostListResponse readUserPostList(long userId);
    byte[] downloadPostImage(String imageURL);
}
