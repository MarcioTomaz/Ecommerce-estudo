package com.ec.ecommercev3.DTO.Notification;

import com.ec.ecommercev3.Entity.Enums.NotificationType;
import com.ec.ecommercev3.Entity.Enums.ReferenceType;

import java.time.Instant;

public record NotificationEvent(
        Long userId,
        String title,
        String message,
        Boolean isRead,
        NotificationType type,
        Long referenceId,
        ReferenceType referenceType, // produto | pedido | etc
        Instant timestamp
) {}