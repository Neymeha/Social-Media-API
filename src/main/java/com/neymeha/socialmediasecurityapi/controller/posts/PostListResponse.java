package com.neymeha.socialmediasecurityapi.controller.posts;

import com.neymeha.socialmediasecurityapi.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    private List<Post> postList;
}
