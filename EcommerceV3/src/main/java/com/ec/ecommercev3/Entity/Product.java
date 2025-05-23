package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    private Integer stock;
}
