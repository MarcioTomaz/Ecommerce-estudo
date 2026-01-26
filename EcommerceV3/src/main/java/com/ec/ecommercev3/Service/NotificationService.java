package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.Entity.Notification;
import com.ec.ecommercev3.Repository.mongo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Page<Notification> getNotificationsByUserId(Long userId, Pageable pageable) {

        return notificationRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
    }

    public void readNotification(Notification notification) {

        Notification result = notificationRepository.findById(notification.getId());

        if(result != null){
            result.setIsRead(true);
            notificationRepository.save(result);
        }
    }
}
