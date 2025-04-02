package com.ec.ecommercev3.DTO.Order;


import java.util.List;


public record OrderSumaryDTO(
        List<OrderItemsDTO> orderItemsDTO,
        Double cart
) {
}