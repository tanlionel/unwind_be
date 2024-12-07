package com.capstone.unwind.controller;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.NotificationDTO.FcmTokenRequest;
import com.capstone.unwind.service.ServiceInterface.FcmService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final FcmService fcmService;

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


}
