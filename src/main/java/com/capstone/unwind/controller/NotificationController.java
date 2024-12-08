package com.capstone.unwind.controller;

import com.capstone.unwind.entity.Notification;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.NotificationDTO.FcmTokenRequest;
import com.capstone.unwind.service.ServiceInterface.FcmService;
import com.capstone.unwind.service.ServiceInterface.NotificationService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final FcmService fcmService;
    @Autowired
    private final NotificationService notificationService;

    @PostMapping("/subcribe-token")
    public ResponseEntity<Boolean> subcribeFcmToken(@RequestBody FcmTokenRequest request) throws OptionalNotFoundException {
        Boolean result = userService.saveFcmToken(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("test-send")
    public ResponseEntity<String> sendTestNotification(@RequestParam String token , @RequestBody String message){
        return ResponseEntity.ok(fcmService.pushNotification(token,"test",message));
    }

    @PostMapping("/staff/subscribe-token")
    public String subscribeToTopic(@RequestParam("token") String token, @RequestParam("topic") String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                    java.util.Collections.singletonList(token),
                    topic
            );
            if (response.getSuccessCount() > 0) {
                return "Successfully subscribed to topic: " + topic;
            } else {
                return "Failed to subscribe to topic. Errors: " + response.getErrors();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error subscribing to topic: " + e.getMessage();
        }
    }

    @GetMapping("/send-notification/topic")
    public ResponseEntity<String> sendNotificationToTopic(
            @RequestParam String topic,
            @RequestParam String title,
            @RequestParam String body) throws FirebaseMessagingException {
        return ResponseEntity.ok(fcmService.pushNotificationTopic(title,body,topic));
    }

    @GetMapping("/customer")
    public Page<Notification> getAllNotificationCustomer(@RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                                         @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        Page<Notification> notificationList = notificationService.getAllNotificationCustomer(pageNo,pageSize);
        return notificationList;
    }

    @GetMapping("/topic")
    public Page<Notification> getAllNotificationTopic(@RequestParam String topic,
                                                         @RequestParam(required = false,defaultValue = "0") Integer pageNo,
                                                         @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        Page<Notification> notificationList = notificationService.getAllNotificationByTopic(topic,pageNo,pageSize);
        return notificationList;
    }


    @PostMapping("/mark-read/{notiId}")
    public Notification markNotificationReadByNotiId(@PathVariable Integer notiId) throws OptionalNotFoundException {
        Notification notification = notificationService.markReadById(notiId);
        return notification;
    }

    @PostMapping("mark-read/all/user/{userId}")
    public Boolean markNotificationReadByUserId(@PathVariable Integer userId){
        Boolean isSuccess = notificationService.markAllReadByUserId(userId);
        return isSuccess;
    }

    @PostMapping("mark-read/all/topic")
    public Boolean markNotificationReadByUserId(@RequestParam String topic){
        Boolean isSuccess = notificationService.markAllReadByTopic(topic);
        return isSuccess;
    }



}
