package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Post {

    @Id
    @GeneratedValue
    private Long postId;
    private String title;
    private String body;
    private String imageURL;
    private Timestamp timestamp;

    public void updatePost(String newTitle, String newBody, String newImage){
        this.setTitle(newTitle);
        this.setBody(newBody);
        this.setImageURL(newImage);
    }
}
