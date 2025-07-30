package com.ec.ecommercev3.Service.helper;

import com.ec.ecommercev3.Entity.Item;
import com.ec.ecommercev3.Entity.Product.Product;
import com.ec.ecommercev3.Repository.Jpa.ProductRepository;
import com.ec.ecommercev3.Service.exceptions.InsufficientStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class StockValidator {

    @Autowired
    private ProductRepository productRepository;

    public void validateStock(List<Item> items){
        List<Long> productIds = items.stream()
                .map(i -> i.getProduct().getId())
                .toList();

        Map<Long, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (Item item : items) {
            Product product = productMap.get(item.getProduct().getId());
            long qty = item.getQuantity();

            if (product.getStock() < qty) {
                throw new InsufficientStockException("Estoque insuficiente para o produto: " + product.getProduct_name());
            }

            product.setStock(product.getStock() - (int) qty);
        }

        productRepository.saveAll(productMap.values());
    }
}
