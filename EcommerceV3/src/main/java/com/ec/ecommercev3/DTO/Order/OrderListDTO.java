package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;

public record OrderListDTO(Long id,
                           OrderStatus status,
                           Double total) {
}
