package com.ec.ecommercev3.Service.helper;

import com.ec.ecommercev3.Entity.Item;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class OrderCalculator {
    public BigDecimal calculateTotal(List<Item> items) {
        double total = items.stream()
                .mapToDouble(i -> i.getQuantity() * i.getProduct().getProduct_price())
                .sum();

        return BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
    }
}