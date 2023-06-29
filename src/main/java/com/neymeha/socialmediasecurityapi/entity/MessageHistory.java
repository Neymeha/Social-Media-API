package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class MessageHistory {
    @Id
    @GeneratedValue
    private long id;
    private String historyStorageURL;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message{
        private String userNameFrom;
        private String userNameTo;
        private Timestamp sendTime;
        private String text;
    }
}
