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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;


// способ хранение неподходит, нужно обеспечить выгрузку картинок отдельно от постов а фронт должен получать только
// юрл уже готовой картинки для ее загрузки или какой либо уникальный айдишник
// так же мне не нравится момент проверки поиска поста чисто по айди, тут есть скрытая проблема если вдруг
// юзер сможет нажать удалить пост на чужом посте он удалит свой если у него есть тот же айди, надо подумать
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final static String absaluteFolderPath = System.getProperty("user.dir") + "/src/main/resources/static/images/";

    @Override
    public PostResponse createPost(String title, String body, MultipartFile image){

        if(title==null || body==null || image==null){
            throw new RequiredFieldsAreNotFilledException("All fields must be filled!", HttpStatus.BAD_REQUEST);
        }
        String type = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".")+1);
        if (!type.equals("gif") && !type.equals("jpeg") && !type.equals("png") && !type.equals("jpg")) {
            throw new RequestException("Image should be gif, png or jpeg, jpg!", HttpStatus.BAD_REQUEST);
        }
        String email = jwtService.extractUsernameFromAuthJwt();
        var user = userRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = Post.builder()
                .title(title)
                .body(body)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        user.createPost(post);
        postRepository.save(post);
        String path = absaluteFolderPath+user.getUserId()+"/"+post.getPostId()+"/";
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String absaluteFilePath = path + image.getOriginalFilename(); // нужно получше настроить иерархию файловой системы по папкам а то будет жесткий бардак
        try {
            image.transferTo(new File(absaluteFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        post.setImage(user.getUserId()+"/"+post.getPostId()+"/"+image.getOriginalFilename());
        postRepository.save(post);
        return PostResponse.builder().post(post).build();
    }

    @Override // не находит посты
    public PostResponse deletePost(long postId){

        if (postId<1) {
            throw new RequestException("Post id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = user.getPosts().stream()
                .filter(p -> p.getPostId() == postId)
                .findAny()
                .orElseThrow(() -> new PostNotFoundException("Post not found", HttpStatus.NOT_FOUND));
        String imageUrl = post.getImage();
        File image = new File(absaluteFolderPath + imageUrl);
        File directoryPost = new File(absaluteFolderPath + user.getUserId()+ "/"+post.getPostId());
        image.delete();
        directoryPost.delete();
        user.deletePost(post);
        postRepository.delete(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostResponse updatePost(long postId, String title, String body, MultipartFile image){

        if (postId<1) {
            throw new RequestException("Post id could not be less than 1", HttpStatus.BAD_REQUEST);
        }
        if(title==null && body==null && image==null){
            throw new RequiredFieldsAreNotFilledException("Nothing to change", HttpStatus.BAD_REQUEST);
        }
        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var post = user.getPosts().stream()
                .findAny()
                .filter(p -> p.getPostId() == postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found!", HttpStatus.NOT_FOUND));
        String imageUrl;
        String type = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".")+1);
        if (!type.equals("gif") && !type.equals("jpeg") && !type.equals("png") && !type.equals("jpg")) {
            throw new RequestException("Image should be gif, png or jpeg, jpg!", HttpStatus.BAD_REQUEST);
        }
        if (!image.isEmpty()) {
            String absaluteFilePath = absaluteFolderPath + user.getUserId()+ "/"+post.getPostId()+"/" + image.getOriginalFilename();
            try {
                image.transferTo(new File(absaluteFilePath));// нужно получше настроить иерархию файловой системы по папкам а то будет жесткий бардак
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageUrl = user.getUserId()+"/"+post.getPostId()+"/"+ image.getOriginalFilename();
            String previousImageUrl = post.getImage();
            File previousImage = new File(absaluteFolderPath+previousImageUrl);
            previousImage.delete();
        } else {
            imageUrl = post.getImage();
        }
        post.updatePost(title, body, imageUrl);
        postRepository.save(post);
        return PostResponse.builder().post(post).build();
    }

    @Override
    public PostListResponse readUserPostList(long userId) {
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

    @Override
    public byte[] downloadPostImage(String imageURL) {
        if (imageURL==null || imageURL.isEmpty()) {
            throw new RequestException("imageURL can not be null or empty", HttpStatus.BAD_REQUEST);
        }
        byte[] image;
        try {
             image = Files.readAllBytes(new File(absaluteFolderPath+imageURL).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
}
