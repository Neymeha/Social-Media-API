package com.neymeha.socialmediasecurityapi.controller.posts;

import com.neymeha.socialmediasecurityapi.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/social/media/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService service;

    @PostMapping()
    public ResponseEntity<PostResponse> createUserPost(@RequestParam String title,
                                                       @RequestParam String body,
                                                       @RequestParam MultipartFile image){
        return new ResponseEntity(service.createPost(title, body, image), HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<PostResponse> refreshUserPost(@RequestParam long postId,
                                                        @RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String body,
                                                        @RequestParam(required = false) MultipartFile image){
        return new ResponseEntity(service.updatePost(postId, title, body, image), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<PostResponse> deleteUserPost(@RequestParam long postId){
        return new ResponseEntity(service.deletePost(postId), HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<PostListResponse> readUserPostList(@RequestParam long userId){
        return new ResponseEntity(service.readUserPostList(userId), HttpStatus.OK);
    }
}
