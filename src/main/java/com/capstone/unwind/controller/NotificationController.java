package com.capstone.unwind.controller;

import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.model.NotificationDTO.FcmTokenRequest;
import com.capstone.unwind.service.ServiceInterface.FcmService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

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
        return ResponseEntity.ok(fcmService.pushNotification(token,message));
    }

    @PostMapping("/staff/subscribe-token")
    public String subscribeToTopic(@RequestParam("token") String token, @RequestParam("topic") String topic) {
        try {
            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                    java.util.Collections.singletonList(token),
                    topic
            );

            // Kiểm tra số lượng thành công và thất bại
            if (response.getSuccessCount() > 0) {
                return "Successfully subscribed to topic: " + topic;
            } else {
                // In chi tiết lỗi nếu có
                return "Failed to subscribe to topic. Errors: " + response.getErrors();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error subscribing to topic: " + e.getMessage();
        }
    }

    @GetMapping("/send-notification/topic")
    public String sendNotificationToTopic(
            @RequestParam String topic,
            @RequestParam String title,
            @RequestParam String body) {

        try {
            // Tạo Notification object
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            // Tạo Message object với topic, sử dụng notification
            Message message = Message.builder()
                    .setNotification(notification)
                    .setTopic(topic)
                    .build();

            // Gửi thông báo tới topic
            String response = FirebaseMessaging.getInstance().send(message);

            return "Successfully sent message: " + response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending message to topic: " + e.getMessage();
        }
    }


}
