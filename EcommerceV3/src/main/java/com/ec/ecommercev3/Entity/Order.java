package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "_order")
@Entity
public class Order extends DomainEntity {

    @ManyToOne
    @JsonIgnore// evita loop infinito
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "billing_address_id", nullable = false)
    private Address billingAddress; // faturamento

    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;// entrega

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    private String coupon;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Double total;
}