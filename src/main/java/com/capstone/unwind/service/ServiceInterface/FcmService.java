package com.capstone.unwind.service.ServiceInterface;

import com.google.firebase.messaging.FirebaseMessagingException;

public interface FcmService {
    public String pushNotification(String token,String title,String content);
    public String pushNotificationTopic(String title, String content,String topic) throws FirebaseMessagingException;

}
