package com.ec.ecommercev3.DTO.Product;

import com.ec.ecommercev3.Entity.Currency;
import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductListDTO {

    private Long id;

    private String product_name;

    private String product_description;

    private double product_price;

    private String image_path;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private Currency currency;

    private Integer stock;

    private Integer version;

}
