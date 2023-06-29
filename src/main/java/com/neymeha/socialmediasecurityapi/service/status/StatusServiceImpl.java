package com.neymeha.socialmediasecurityapi.service.status;

import com.neymeha.socialmediasecurityapi.controller.status.StatusRequest;
import com.neymeha.socialmediasecurityapi.controller.status.StatusResponse;
import com.neymeha.socialmediasecurityapi.customexceptions.request.RequestException;
import com.neymeha.socialmediasecurityapi.customexceptions.request.StatusRequestAlreadyExistsException;
import com.neymeha.socialmediasecurityapi.customexceptions.user.UserNotFoundException;
import com.neymeha.socialmediasecurityapi.entity.Status;
import com.neymeha.socialmediasecurityapi.repository.UserRepository;
import com.neymeha.socialmediasecurityapi.repository.StatusRepository;
import com.neymeha.socialmediasecurityapi.service.auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final UserRepository userRepository;
    private final StatusRepository statusRepository;
    private final JwtService jwtService;

    @Override
    public StatusResponse friendAddRequest(StatusRequest request){
        targetIdCheck(request.getTargetUserId());
        var mainUser = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var targetUser = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        Status sts;
        if (mainUser.getStatusWithUsers().containsKey(targetUser)){
            sts = mainUser.getStatusWithUsers().get(targetUser);
            if (sts.isSubscription()){
                throw new StatusRequestAlreadyExistsException("Request is already send or got denied!", HttpStatus.ALREADY_REPORTED);
            } // нужно сделать счетчик что бы понять был отказ или запрос еще активен
            sts.setSubscription(true);
            mainUser.getStatusWithUsers().replace(targetUser, sts);
            targetUser.recieveFriendRequest(mainUser.getUserId());
        } else {
            sts = Status.builder()
                    .friend(false)
                    .subscription(true)
                    .build();
            mainUser.addToFriendList(targetUser, sts);
            targetUser.recieveFriendRequest(mainUser.getUserId());
        }
        statusRepository.save(sts);
        return StatusResponse.builder()
                .status(sts)
                .build();
    }

    @Override
    public StatusResponse friendAddConfirm(StatusRequest request){
        targetIdCheck(request.getTargetUserId());
        var mainUser = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var targetUser = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        Status stsMain = mainUser.getStatusWithUsers().get(targetUser);
        if (stsMain==null){
            stsMain = Status.builder()
                    .friend(true)
                    .subscription(true)
                    .build();
        }
        Status stsTarget = targetUser.getStatusWithUsers().get(mainUser);
        stsTarget.setFriend(true);
        mainUser.replyFriendRequest(targetUser.getUserId());
        mainUser.addToFriendList(targetUser, stsMain);
        statusRepository.save(stsMain);
        statusRepository.save(stsTarget);
        return StatusResponse.builder()
                .status(stsMain)
                .build();
    }

    @Override
    public StatusResponse friendAddRefuse(StatusRequest request){
        targetIdCheck(request.getTargetUserId());
        var user = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        user.replyFriendRequest(request.getTargetUserId());
        userRepository.save(user);
        return StatusResponse.builder()
                .build();
    }

    @Override
    public StatusResponse friendDelete(StatusRequest request){
        targetIdCheck(request.getTargetUserId());
        var mainUser = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var targetUser = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        Status stsMain = mainUser.getStatusWithUsers().get(targetUser);
        Status stsTarget = targetUser.getStatusWithUsers().get(mainUser);
        stsMain.setFriend(false);
        stsMain.setSubscription(false);
        stsTarget.setFriend(false);
        mainUser.deleteFriend(targetUser, stsMain);
        targetUser.deleteFriend(mainUser, stsTarget);
        statusRepository.save(stsMain);
        statusRepository.save(stsTarget);
        return StatusResponse.builder()
                .status(stsMain)
                .build();
    }

    @Override
    public StatusResponse subscriptionDelete(StatusRequest request){
        targetIdCheck(request.getTargetUserId());
        var mainUser = userRepository.findByEmail(jwtService.extractUsernameFromAuthJwt()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        var targetUser = userRepository.findById(request.getTargetUserId()).orElseThrow(()->new UserNotFoundException("User not found in DB!", HttpStatus.NOT_FOUND));
        Status stsMain = mainUser.getStatusWithUsers().get(targetUser);
        stsMain.setSubscription(false);
        mainUser.deleteFriend(targetUser, stsMain);
        statusRepository.save(stsMain);
        return StatusResponse.builder()
                .status(stsMain)
                .build();
    }

    private void targetIdCheck(long targetUserId){
        if (targetUserId<1){
            throw new RequestException("Target user id could not be less than 1", HttpStatus.BAD_REQUEST);
        }
    }
}
