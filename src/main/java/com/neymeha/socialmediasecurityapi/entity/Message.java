package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    @Id
    @GeneratedValue
    long id;
    private String userNameFrom;
    private String userNameTo;
    private Timestamp sendTime;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message message)) return false;
        return getId() == message.getId() && Objects.equals(getUserNameFrom(), message.getUserNameFrom()) && Objects.equals(getUserNameTo(), message.getUserNameTo()) && Objects.equals(getSendTime(), message.getSendTime()) && Objects.equals(getText(), message.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserNameFrom(), getUserNameTo(), getSendTime(), getText());
    }
}
