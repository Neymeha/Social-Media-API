package com.neymeha.socialmediasecurityapi.service.status;

import com.neymeha.socialmediasecurityapi.controller.status.StatusRequest;
import com.neymeha.socialmediasecurityapi.controller.status.StatusResponse;

public interface StatusService {
    StatusResponse friendAddRequest(StatusRequest request);
    StatusResponse friendAddConfirm(StatusRequest request);
    StatusResponse friendAddRefuse(StatusRequest request);
    StatusResponse friendDelete(StatusRequest request);
    StatusResponse subscriptionDelete(StatusRequest request);
}
