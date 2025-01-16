package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;


public record OrderDTO(
        Long person,
        Long billingAddress,
        Long shippingAddress,
        Long cart,
//        String coupon,
        String status //pendente, enviado, etc...
) {
}