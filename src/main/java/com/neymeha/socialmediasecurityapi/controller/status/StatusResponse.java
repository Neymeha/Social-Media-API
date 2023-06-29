package com.neymeha.socialmediasecurityapi.controller.status;

import com.neymeha.socialmediasecurityapi.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    private Status status;
}
