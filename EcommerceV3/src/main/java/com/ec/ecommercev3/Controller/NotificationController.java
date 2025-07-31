//package com.ec.ecommercev3.Controller;
//
//import com.ec.ecommercev3.Entity.Notification;
//import com.ec.ecommercev3.Entity.UserPerson;
//import com.ec.ecommercev3.Service.NotificationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/api/notification")
//public class NotificationController {
//
//    @Autowired
//    private NotificationService notificationService;
//
//    @GetMapping("")
//    public ResponseEntity<Page<Notification>> getNotificationForUser(
//            @AuthenticationPrincipal UserPerson userPerson, Pageable pageable) {
//
//        Page<Notification> result = notificationService.getNotificationsByUserId(userPerson.getId(), pageable);
//
//        return ResponseEntity.ok(result);
//    }
//
//}
