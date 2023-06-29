package com.neymeha.socialmediasecurityapi.controller.status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequest {
    private long targetUserId;
}
