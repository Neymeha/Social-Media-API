package com.neymeha.socialmediasecurityapi.service.message;

import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryRequest;
import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryResponse;

public interface MessageService {
    HistoryResponse getHistoryWithFriend(HistoryRequest request);
}
