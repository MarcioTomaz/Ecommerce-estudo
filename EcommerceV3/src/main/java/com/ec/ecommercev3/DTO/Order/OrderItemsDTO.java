package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.DTO.Product.ProductEditDTO;
import com.ec.ecommercev3.Entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsDTO {

    private Long orderId;

    private ProductEditDTO product;

    private Long quantity;

}
