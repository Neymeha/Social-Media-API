package com.neymeha.socialmediasecurityapi.repository;

import com.neymeha.socialmediasecurityapi.entity.MessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
}
