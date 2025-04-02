package com.ec.ecommercev3.DTO.Order;

public record OrderStepOneDTO(
        Long billingAddress,
        Long shippingAddress
) {
}