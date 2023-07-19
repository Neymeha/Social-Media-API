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
    private String image;
    private Timestamp timestamp;

    public void updatePost(String newTitle, String newBody, String newImage){
        if (!this.body.equals(newBody) && newTitle!=null && !newTitle.isEmpty()){
            this.setBody(newBody);
        }
        if (!this.title.equals(newTitle) && newTitle!=null && !newTitle.isEmpty()){
            this.setTitle(newTitle);
        }
        if (!this.image.equals(newImage) && newTitle!=null && !newTitle.isEmpty()){
            this.setImage(newImage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getBody(), post.getBody()) && Objects.equals(getImage(), post.getImage()) && Objects.equals(getTimestamp(), post.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getBody(), getImage(), getTimestamp());
    }
}
