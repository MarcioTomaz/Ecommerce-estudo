package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table( name= "_product")
public class Product extends DomainEntity{

    private String product_name;

    private String product_description;

    private double product_price;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    private Integer stock_quantity;
}
