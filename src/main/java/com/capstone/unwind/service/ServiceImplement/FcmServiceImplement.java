package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.service.ServiceInterface.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FcmServiceImplement implements FcmService {

    @Override
    public String pushNotification(String token,String title,String content) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(content)
                .build();
        Message messageSend = Message.builder()
                .setNotification(notification)
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

    @Override
    public String pushNotificationTopic(String title, String content,String topic) throws FirebaseMessagingException {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(content)
                .build();
        try{
            Message message = Message.builder()
                    .setNotification(notification)
                    .setTopic(topic)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
        return "Successfully sent message: " + response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending message to topic: ";
        }
    }
}
