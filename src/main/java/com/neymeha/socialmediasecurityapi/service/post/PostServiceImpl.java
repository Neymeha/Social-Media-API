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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final static String absaluteFolderPath = System.getProperty("user.dir") + "/src/main/resources/static/images/";

    @Override
    public PostResponse createPost(String title, String body, MultipartFile image){

        if(title==null || body==null || image==null || title.isEmpty() || body.isEmpty() || image.isEmpty()){
            throw new RequiredFieldsAreNotFilledException("All fields must be filled!", HttpStatus.BAD_REQUEST);
        }
        String email = jwtService.extractUsernameFromAuthJwt();
        var user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = Post.builder()
                .title(title)
                .body(body)
                .imageURL(image.getOriginalFilename())
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        String absaluteFilePath = absaluteFolderPath + image.getOriginalFilename(); // нужно получше настроить иерархию файловой системы по папкам а то будет жесткий бардак
        try {
            image.transferTo(new File(absaluteFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        user.createPost(post);
        postRepository.save(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostResponse deletePost(long postId){

        if (postId<1) {
            throw new RequestException("Post id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = user.getPosts().stream()
                .findAny()
                .filter(p -> p.getPostId() == postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found", HttpStatus.NOT_FOUND));
        String imageUrl = post.getImageURL();
        File image = new File(absaluteFolderPath+imageUrl);
        image.delete();
        user.deletePost(post);
        postRepository.delete(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostResponse updatePost(long postId, String title, String body, MultipartFile image){

        if (postId<1) {
            throw new RequestException("Post id could not be less than 1", HttpStatus.BAD_REQUEST);
        }
        if(title==null || body==null || title.isEmpty() || body.isEmpty()){
            throw new RequiredFieldsAreNotFilledException("Title and body of the Post can not be empty or null", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = user.getPosts().stream()
                .findAny()
                .filter(p -> p.getPostId() == postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found!", HttpStatus.NOT_FOUND));
        String imageUrl;
        if (image!=null || !image.isEmpty()) {
            String absaluteFilePath = absaluteFolderPath + image.getOriginalFilename();
            try {
                image.transferTo(new File(absaluteFilePath));// нужно получше настроить иерархию файловой системы по папкам а то будет жесткий бардак
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageUrl = image.getOriginalFilename();
            String previousImageUrl = post.getImageURL();
            File previousImage = new File(absaluteFolderPath+previousImageUrl);
            previousImage.delete();
        } else {
            imageUrl = post.getImageURL();
        }
        post.updatePost(title, body, imageUrl);
        postRepository.save(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostListResponse readUserPostList(long userId){
        if(userId<1){
            throw new RequestException("User id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        return PostListResponse.builder()
                .postList(userRepository
                        .findById(userId)
                        .orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND))
                        .getPosts())
                .build();
    }
}
