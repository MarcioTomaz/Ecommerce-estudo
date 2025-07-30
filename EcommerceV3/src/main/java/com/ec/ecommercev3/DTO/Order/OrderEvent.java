package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonLOG;
import com.ec.ecommercev3.Entity.Enums.AlteredByType;
import com.ec.ecommercev3.Entity.Enums.ExecutionType;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.time.Instant;

public record OrderEvent(
        Long orderId,
        OrderStatus status,
        String refuseReason,
        UserPersonLOG userLog,
        Instant timestamp,
        AlteredByType alteredByType,
        ExecutionType executionType
) {
}
