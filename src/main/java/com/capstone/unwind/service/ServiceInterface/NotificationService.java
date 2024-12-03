package com.capstone.unwind.service.ServiceInterface;

public interface NotificationService {
    public void sendNotification(String message);
    void sendNotificationToChannel(String channel, String message);
    void sendNotificationToUser( String message);

}