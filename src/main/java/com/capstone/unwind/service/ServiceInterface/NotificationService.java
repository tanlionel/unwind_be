package com.capstone.unwind.service.ServiceInterface;


import com.capstone.unwind.entity.Notification;
import com.capstone.unwind.exception.OptionalNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    Page<Notification> getAllNotificationCustomer(Integer pageNo, Integer pageSize);

    Page<Notification> getAllNotificationByTopic(String topic,Integer pageNo, Integer pageSize);

    Notification markReadById(Integer notiId) throws OptionalNotFoundException;

    Boolean markAllReadByUserId(Integer userId);

    Boolean markAllReadByTopic(String topic);

}
