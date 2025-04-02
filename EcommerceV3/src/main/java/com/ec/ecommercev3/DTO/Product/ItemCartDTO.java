package com.ec.ecommercev3.DTO.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCartDTO {

    private Long productID;

    private int quantity;
}
