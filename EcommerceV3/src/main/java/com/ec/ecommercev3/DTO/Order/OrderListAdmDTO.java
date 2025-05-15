package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.time.LocalDate;

public record OrderListAdmDTO(Long id,
                              OrderStatus status,
                              LocalDate orderDate,
                              Double total) {
}
