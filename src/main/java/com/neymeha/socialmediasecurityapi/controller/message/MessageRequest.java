package com.neymeha.socialmediasecurityapi.controller.message;

import com.neymeha.socialmediasecurityapi.entity.MessageHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {
    private String message;
    private long targetUserId;
}
