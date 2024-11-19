package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.service.ServiceInterface.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImplement implements NotificationService {
    @Autowired
    private SimpMessagingTemplate template;
    @Override
    public void sendNotification(String message) {
        template.convertAndSend("/topic/notifications", message);
    }
}
