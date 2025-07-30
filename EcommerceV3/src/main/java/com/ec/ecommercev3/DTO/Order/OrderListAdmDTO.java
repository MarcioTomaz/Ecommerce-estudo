package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderListAdmDTO(Long id,
                              OrderStatus status,
                              LocalDateTime orderDate,
                              Double total) {
}
