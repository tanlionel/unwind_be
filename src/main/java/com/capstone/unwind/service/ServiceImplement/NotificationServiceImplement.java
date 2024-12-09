package com.capstone.unwind.service.ServiceImplement;

import com.capstone.unwind.entity.User;
import com.capstone.unwind.entity.Notification;
import com.capstone.unwind.exception.OptionalNotFoundException;
import com.capstone.unwind.repository.NotificationRepository;
import com.capstone.unwind.repository.UserRepository;
import com.capstone.unwind.service.ServiceInterface.NotificationService;
import com.capstone.unwind.service.ServiceInterface.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImplement implements NotificationService {
    @Autowired
    private final NotificationRepository notificationRepository;
    @Autowired
    private final UserService userService;
    

    @Override
    public Page<Notification> getAllNotificationCustomer(Integer pageNo, Integer pageSize) {
        User user = userService.getLoginUser();
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("createdAt").descending());
        Page<Notification> notificationList = notificationRepository.findAllByUserId(user.getId(),pageable);
        return notificationList;
    }

    @Override
    public Page<Notification> getAllNotificationByTopic(String topic, Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by("createdAt").descending());
        Page<Notification> notificationList = notificationRepository.findAllByRole(topic,pageable);
        return notificationList;
    }

    @Override
    public Notification markReadById(Integer notiId) throws OptionalNotFoundException {
        Notification  notification = notificationRepository.findById(notiId).orElseThrow(()->new OptionalNotFoundException("not found notification"));
        notification.setIsRead(true);
        Notification notificationInDb = notificationRepository.save(notification);
        return notificationInDb;
    }

    @Override
    public Boolean markAllReadByUserId(Integer userId) {
        notificationRepository.markAllReadByUserId(userId);
        return true;
    }

    @Override
    public Boolean markAllReadByTopic(String topic) {
        notificationRepository.markAllReadByTopic(topic);
        return true;
    }



}
