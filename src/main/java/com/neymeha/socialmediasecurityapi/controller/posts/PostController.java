package com.neymeha.socialmediasecurityapi.controller.posts;

import com.neymeha.socialmediasecurityapi.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/social/media/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping()
    public ResponseEntity<PostResponse> createUserPost(@RequestBody CreatePostRequest request){
        return new ResponseEntity(service.createPost(request), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<PostResponse> refreshUserPost(@RequestBody UpdatePostRequest request){
        return new ResponseEntity(service.updatePost(request), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<PostResponse> deleteUserPost(@RequestBody DeletePostRequest request){
        return new ResponseEntity(service.deletePost(request), HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<PostListResponse> readUserPostList(@RequestBody ReadPostListRequest request){
        return new ResponseEntity(service.readUserPostList(request), HttpStatus.OK);
    }
}
