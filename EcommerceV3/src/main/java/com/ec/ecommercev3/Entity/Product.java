package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
