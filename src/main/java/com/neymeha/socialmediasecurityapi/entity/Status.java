package com.neymeha.socialmediasecurityapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Status {
    @Id
    @GeneratedValue
    private Long statusId;
    private boolean friend;
    private boolean subscription;

    public boolean mySubOrNot(Status status){
        return isSubscription()==status.isSubscription();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status status)) return false;
        return isFriend() == status.isFriend() && isSubscription() == status.isSubscription() && Objects.equals(getStatusId(), status.getStatusId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatusId(), isFriend(), isSubscription());
    }
}
