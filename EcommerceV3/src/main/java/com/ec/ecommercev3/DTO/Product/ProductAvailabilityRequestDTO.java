package com.ec.ecommercev3.DTO.Product;

import java.time.Instant;

public record ProductAvailabilityRequestDTO(
        Long userId,
        Long productId,
        String productName,
        Instant timestamp
) {
}
