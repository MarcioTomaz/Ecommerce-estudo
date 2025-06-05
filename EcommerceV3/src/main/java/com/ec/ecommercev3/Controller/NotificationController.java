package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.Entity.Notification;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<Notification>> getNotificationForUser(
            @AuthenticationPrincipal UserPerson userPerson) {

        List<Notification> result = notificationService.getNotificationsByUserId(userPerson.getId());

        return ResponseEntity.ok(result);
    }

}
