package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.Entity.Enums.NotificationType;
import com.ec.ecommercev3.Entity.Enums.ReferenceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification extends DomainEntity{

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private Boolean isRead = false;

    private LocalDateTime readAt;

    private NotificationType type;

    private Long referenceId; // ex: orderId
    private ReferenceType referenceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserPerson user;

}
