package com.neymeha.socialmediasecurityapi.service.message;

import com.neymeha.socialmediasecurityapi.controller.message.MessageRequest;
import com.neymeha.socialmediasecurityapi.controller.message.MessageResponse;
import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryLoadRequest;
import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryResponse;
import com.neymeha.socialmediasecurityapi.customexceptions.message.MessageException;
import com.neymeha.socialmediasecurityapi.customexceptions.message.NoHistoryFoundException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.status.StatusException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.entity.Message;
import com.neymeha.socialmediasecurityapi.entity.MessageHistory;
import com.neymeha.socialmediasecurityapi.entity.Status;
import com.neymeha.socialmediasecurityapi.repository.MessageHistoryRepository;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final UserRepository userRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final JwtService jwtService;
    private final static String absaluteFolderPath = System.getProperty("user.dir") + "/src/main/resources/static/messagehistory/";

    @Override
    public MessageResponse sendAndSaveMessage(MessageRequest request) {
        if (request.getTargetUserId()<1) {
            throw new UserException("Target user id can not be less than 1", HttpStatus.BAD_REQUEST);
        } else if (request.getMessage()==null || request.getMessage().isEmpty()) {
            throw new MessageException("Message can not be null or empty", HttpStatus.BAD_REQUEST);
        }

        var mainUser = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("Main user not found in DB", HttpStatus.NOT_FOUND));
        var targetUser = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("Target user not found in DB", HttpStatus.NOT_FOUND));

        Status mainSts = mainUser.getStatusWithUsers().get(targetUser);
        Status targetSts = targetUser.getStatusWithUsers().get(mainUser);

        if (!mainSts.isFriend() && !targetSts.isFriend()) {
            throw new StatusException("You are not friends with that user", HttpStatus.BAD_REQUEST);
        }

        MessageHistory mainHistory = mainUser.getMessageHistories().get(targetUser);
        MessageHistory targetHistory = targetUser.getMessageHistories().get(mainUser);
        MessageHistory history;
        Message message = Message.builder()
                .sendTime(new Timestamp(System.currentTimeMillis()))
                .userNameFrom(mainUser.getName())
                .userNameTo(targetUser.getName())
                .text(request.getMessage())
                .build();
        if (mainHistory==null && targetHistory==null) {
            history = new MessageHistory();
            history.addMessageToHistory(message);
            mainUser.getMessageHistories().put(targetUser, history);
            targetUser.getMessageHistories().put(mainUser, history);
            targetUser.recieveMessageNotification(mainUser.getUserId());
        } else if (mainHistory==null || targetHistory==null){
            throw new MessageException("Message history not found or broken, something went wrong!", HttpStatus.NOT_FOUND);
        } if (mainHistory.equals(targetHistory)) {
            history = mainHistory;
            history.addMessageToHistory(message);
            targetUser.recieveMessageNotification(mainUser.getUserId());
        } else {
            throw new MessageException("Something went wrong!", HttpStatus.NOT_FOUND);
        }
        return MessageResponse.builder()
                .message(message)
                .build();
    }

    @Override
    public HistoryResponse getHistoryWithFriend(HistoryLoadRequest request){

        if (request.getTargetUserId()<1){
            throw new RequestException("Target user id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var mainUser = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var targetUser = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("Target user not found in DB!", HttpStatus.NOT_FOUND));

        MessageHistory mainHistory = mainUser.getMessageHistories().get(targetUser);
        MessageHistory targetHistory = targetUser.getMessageHistories().get(mainUser);
        List<Message> messageList;

        if (mainHistory==null && targetHistory==null){
            throw new NoHistoryFoundException("Message history with user not found!", HttpStatus.NOT_FOUND);
        } else if (mainHistory==null || targetHistory == null) {
            throw new MessageException("Message history not found or broken", HttpStatus.NOT_FOUND);
        } else if (mainHistory.equals(targetHistory)) {
            messageList = mainHistory.getMessageList();
        } else {
            throw new MessageException("Something went wrong!", HttpStatus.NOT_FOUND);
        }

        return HistoryResponse.builder()
                .messageList(messageList)
                .build();
    }
}
