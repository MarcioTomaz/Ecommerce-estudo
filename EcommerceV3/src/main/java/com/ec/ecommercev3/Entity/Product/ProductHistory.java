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
@Table( name= "_product_history")
public class ProductHistory extends DomainEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer version;

    private String product_name;

    private String product_description;

    private double product_price;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    private Integer stock;

    public ProductHistory() {}

    public ProductHistory(Product product) {
        this.product = product;
        this.version = product.getVersion();
        this.product_name = product.getProduct_name();
        this.product_description = product.getProduct_description();
        this.product_price = product.getProduct_price();
        this.productCategory = product.getProductCategory();
        this.currency = product.getCurrency();
        this.stock = product.getStock(); // Grava o estoque daquele momento
    }
}
