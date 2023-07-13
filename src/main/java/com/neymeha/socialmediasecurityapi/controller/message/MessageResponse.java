package com.neymeha.socialmediasecurityapi.controller.message;

import com.neymeha.socialmediasecurityapi.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Message message;
}
