package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonLOG;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.UserPerson;

import java.time.Instant;

public record OrderEvent(
        Long orderId,
        OrderStatus status,
        String refuseReason,
        UserPersonLOG userLog,
        Instant timestamp
) {
}
