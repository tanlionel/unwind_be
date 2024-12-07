package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.service.ServiceInterface.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FcmServiceImplement implements FcmService {

    @Override
    public String pushNotification(String token, String message) {
        Message messageSend = Message.builder()
                .putData("content",message)
                .setToken(token)
                .build();
        String response = null;
        try{
            response = FirebaseMessaging.getInstance().send(messageSend);
        }catch (FirebaseMessagingException e){
            e.printStackTrace();
        }
        return response;
    }
}
