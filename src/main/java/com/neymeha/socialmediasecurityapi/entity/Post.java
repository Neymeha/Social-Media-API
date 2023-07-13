package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getBody(), post.getBody()) && Objects.equals(getImageURL(), post.getImageURL()) && Objects.equals(getTimestamp(), post.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getBody(), getImageURL(), getTimestamp());
    }
}
