package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.Entity.Notification;
import com.ec.ecommercev3.Repository.mongo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getNotificationsByUserId(Long userId) {

        List<Notification> result = notificationRepository.findByUserId(userId);

        return result;
    }

}
