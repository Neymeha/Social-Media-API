package com.neymeha.socialmediasecurityapi.service.post;

import com.neymeha.socialmediasecurityapi.controller.posts.*;

public interface PostService {
    PostResponse createPost(CreatePostRequest request);
    PostResponse deletePost(DeletePostRequest request);
    PostResponse updatePost(UpdatePostRequest request);
    PostListResponse readUserPostList(ReadPostListRequest request);
}
