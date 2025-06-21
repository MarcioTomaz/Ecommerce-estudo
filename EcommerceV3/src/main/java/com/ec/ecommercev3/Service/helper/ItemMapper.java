package com.ec.ecommercev3.Service.helper;

import com.ec.ecommercev3.Entity.Item;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.Product.ProductHistory;
import com.ec.ecommercev3.Repository.Jpa.ProductHistoryRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    public List<Item> map(List<Item> cartItems, Order order) {

        return cartItems.stream().map(
                cartItem ->{
                    ProductHistory lastProductVersion = productHistoryRepository.findTopByProductIdOrderByVersionDesc(cartItem.getProduct().getId())
                            .orElseThrow( () -> new ResourceNotFoundException("Versão do produto não encontrada!"));
                    Item orderItem = new Item();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setProductHistory(lastProductVersion);
                    orderItem.setQuantity(cartItem.getQuantity());

                    return orderItem;
                }).toList();
    }
}
