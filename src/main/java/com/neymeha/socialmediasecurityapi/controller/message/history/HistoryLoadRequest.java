package com.neymeha.socialmediasecurityapi.controller.message.history;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryLoadRequest {
    private long targetUserId;
}
