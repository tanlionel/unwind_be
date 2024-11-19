package com.capstone.unwind.controller;

import com.capstone.unwind.service.ServiceInterface.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @MessageMapping("/send")
    @SendTo("/topic/notifications")
    public String sendNotification(String message) {
        return message;
    }
}
