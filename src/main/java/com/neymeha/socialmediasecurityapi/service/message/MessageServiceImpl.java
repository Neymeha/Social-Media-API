package com.neymeha.socialmediasecurityapi.service.message;

import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryRequest;
import com.neymeha.socialmediasecurityapi.controller.message.history.HistoryResponse;
import com.neymeha.socialmediasecurityapi.customexceptions.message.NoHistoryFoundException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public HistoryResponse getHistoryWithFriend(HistoryRequest request){

        if (request.getTargetUserId()<1){
            throw new RequestException("Target user id could not be less than 1", HttpStatus.BAD_REQUEST);
        }

        var userMain = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var userTarget = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        String historyURL = userMain.getMessageHistories().get(userTarget).getHistoryStorageURL();
        if (historyURL!=null || historyURL.isEmpty()){
            // дальше нужен код что бы достать историю из файловой системе по юрл который получили выше
        } else {
            throw new NoHistoryFoundException("You dont have message history with that User", HttpStatus.NOT_FOUND);
        }
        return HistoryResponse.builder().messageList(null).build();
    }
}
