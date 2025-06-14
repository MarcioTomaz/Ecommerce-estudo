package com.ec.ecommercev3.Service.helper;

import com.ec.ecommercev3.Entity.Item;
import com.ec.ecommercev3.Entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public List<Item> map(List<Item> cartItems, Order order) {

        return cartItems.stream()
                .map( cartItem -> new Item(
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        order
                )).toList();
    }
}
