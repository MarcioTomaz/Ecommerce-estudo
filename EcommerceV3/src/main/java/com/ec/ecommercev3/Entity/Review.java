package com.ec.ecommercev3.Entity;


import com.ec.ecommercev3.Entity.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "_review")
public class Review extends DomainEntity{

    @Column(nullable = false)
    private int rating;

    @Column
    private String feedBack;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id") // Avaliação vinculada ao pedido
    private Order order;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id") // Usuário que fez a avaliação
    private Person customer;
}
