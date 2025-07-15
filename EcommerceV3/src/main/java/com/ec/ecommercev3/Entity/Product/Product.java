package com.ec.ecommercev3.Entity.Product;

import com.ec.ecommercev3.Entity.Currency;
import com.ec.ecommercev3.Entity.DomainEntity;
import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import com.ec.ecommercev3.Entity.Review;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    public Product(Product product) {
        super.setActive(product.isActive());
        this.product_name = product.getProduct_name();
        this.product_description = product.getProduct_description();
        this.product_price = product.getProduct_price();
        this.productCategory = product.getProductCategory();
        this.currency = product.getCurrency();
        this.stock = product.getStock();
        this.version = product.getVersion();
    }
}
