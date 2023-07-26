package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class MessageHistory {
    @Id
    @GeneratedValue
    private long id;

    @OneToMany
    @JoinColumn(name = "history_id")
    private List<Message> messageList;

    public void addMessageToHistory(Message message){
        if (messageList==null){
            messageList = new ArrayList<>();
            messageList.add(message);
        } else {
            messageList.add(message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageHistory history)) return false;
        return getId() == history.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
