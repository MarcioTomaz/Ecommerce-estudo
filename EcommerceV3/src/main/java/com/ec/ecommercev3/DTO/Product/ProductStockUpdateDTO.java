package com.ec.ecommercev3.DTO.Product;

public record ProductStockUpdateDTO(
        Long userId,
        Long productId,
        Integer version,
        Long quantity
){
}
