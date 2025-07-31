package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.Entity.Enums.NotificationType;
import com.ec.ecommercev3.Entity.Enums.ReferenceType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

//@Document(collection = "notification")
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    private String id;

    private Long userId;
    private String title;
    private String message;
    private Boolean isRead;
    private NotificationType type;

    private Long referenceId; // ex: orderId
    private ReferenceType referenceType;
    private Instant timestamp;

}
