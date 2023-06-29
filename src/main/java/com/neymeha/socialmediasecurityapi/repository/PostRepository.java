package com.neymeha.socialmediasecurityapi.repository;

import com.neymeha.socialmediasecurityapi.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
