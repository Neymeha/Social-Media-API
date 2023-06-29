package com.neymeha.socialmediasecurityapi.service.post;

import com.neymeha.socialmediasecurityapi.controller.posts.*;
import com.neymeha.socialmediasecurityapi.customexceptions.post.PostNotFoundException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequiredFieldsAreNotFilledException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.entity.Post;
import com.neymeha.socialmediasecurityapi.repository.PostRepository;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public PostResponse createPost(CreatePostRequest request){

        if(request.getTitle()==null || request.getBody()==null){
            throw new RequiredFieldsAreNotFilledException("All fields must be filled!", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = Post.builder()
                        .title(request.getTitle())
                        .body(request.getBody())
                        .imageURL(request.getImage())
                        .timestamp(new Timestamp(System.currentTimeMillis()))
                        .build();
        user.createPost(post);
        postRepository.save(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostResponse deletePost(DeletePostRequest request){

        if (request.getPostId()<1) {
            throw new RequestException("Post id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = user.getPosts().stream()
                .findAny()
                .filter(p -> p.getPostId() == request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found", HttpStatus.NOT_FOUND));
        user.deletePost(post);
        postRepository.delete(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostResponse updatePost(UpdatePostRequest request){

        if (request.getPostId()<1) {
            throw new RequestException("Post id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = user.getPosts().stream()
                .findAny()
                .filter(p -> p.getPostId() == request.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post not found!", HttpStatus.NOT_FOUND));
        post.updatePost(request.getUpdateTitle(), request.getUpdateBody(), request.getUpdateImage());
        postRepository.save(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostListResponse readUserPostList(ReadPostListRequest request){
        // кого читаем посты? себя или любого пользователя?
        if(request.getUserId()<1){
            throw new RequestException("User id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        return PostListResponse.builder()
                .postList(userRepository
                        .findById(request.getUserId())
                        .orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND))
                        .getPosts())
                .build();
    }
}
