package com.ec.ecommercev3.Entity.Product;

import com.ec.ecommercev3.Entity.Currency;
import com.ec.ecommercev3.Entity.DomainEntity;
import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import com.ec.ecommercev3.Entity.Review;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table( name= "_product")
public class Product extends DomainEntity {

    private String product_name;

    private String product_description;

    private double product_price;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    private Integer stock;

    private Integer version;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
