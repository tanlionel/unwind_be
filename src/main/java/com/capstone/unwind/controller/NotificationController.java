package com.capstone.unwind.controller;

import com.capstone.unwind.service.ServiceInterface.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestParam String message) {
        notificationService.sendNotification(message);
        return ResponseEntity.ok("Notification sent: " + message);
    }
    @PostMapping("/sendToChannel")
    public void sendNotificationToChannel(@RequestParam String channel, @RequestParam String message) {
        notificationService.sendNotificationToChannel(channel, message);
    }
    @PostMapping("/sendToUser/")
    public void sendToUser( @RequestParam String message) {
        notificationService.sendNotificationToUser( message);
    }
}
