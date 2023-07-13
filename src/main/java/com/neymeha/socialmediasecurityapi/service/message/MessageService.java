package com.neymeha.socialmediasecurityapi.service.message;

import com.neymeha.socialmediasecurityapi.controller.message.MessageRequest;
import com.neymeha.socialmediasecurityapi.controller.message.MessageResponse;
import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryLoadRequest;
import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryResponse;

public interface MessageService {
    HistoryResponse getHistoryWithFriend(HistoryLoadRequest request);
    MessageResponse sendAndSaveMessage(MessageRequest request);
}
