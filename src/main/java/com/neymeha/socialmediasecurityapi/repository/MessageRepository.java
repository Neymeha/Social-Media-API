package com.neymeha.socialmediasecurityapi.repository;

import com.neymeha.socialmediasecurityapi.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
