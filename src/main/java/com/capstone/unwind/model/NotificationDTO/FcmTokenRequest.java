package com.capstone.unwind.model.NotificationDTO;

import lombok.Data;

@Data
public class FcmTokenRequest {
    Integer userId;
    String fcmToken;
}
