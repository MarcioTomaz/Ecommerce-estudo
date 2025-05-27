package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderListDTO(Long id,
                           OrderStatus status,
                           LocalDateTime orderDate,
                           Double total) {
}
