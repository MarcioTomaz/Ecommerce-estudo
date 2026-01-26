package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.Entity.Notification;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<Page<Notification>> getNotificationForUser(
            @AuthenticationPrincipal UserPerson userPerson, Pageable pageable) {

        Page<Notification> result = notificationService.getNotificationsByUserId(userPerson.getId(), pageable);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/read")
    public ResponseEntity<Notification> readNotification(@RequestBody Notification notification){
        notificationService.readNotification(notification);
        return ResponseEntity.ok(notification);
    }

}
