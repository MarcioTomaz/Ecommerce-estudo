package com.ec.ecommercev3.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_item")
@Entity
public class Item extends DomainEntity {

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = true) // Relacionamento com Cart
    @JsonBackReference("cart-reference") // Nome único para o back-reference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true) // Relacionamento com Order
    @JsonBackReference("order-reference") // Nome único para o back-reference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Long quantity;

//    @Column
//    private Double total_value;

    public Item(Product product, Long quantity, /*Double totalValue,*/ Order order) {
        this.product = product;
        this.quantity = quantity;
//        this.total_value = totalValue;
        this.order = order;
    }

}
