package com.neymeha.socialmediasecurityapi.controller.message.history;

import com.neymeha.socialmediasecurityapi.entity.MessageHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryResponse {
    private List<MessageHistory.Message> messageList;
}
