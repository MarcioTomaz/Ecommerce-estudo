package com.ec.ecommercev3.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_orderItems")
public class OrderItems extends DomainEntity{

//
//    @ManyToOne
//    @JoinColumn(name = "order_id", nullable = false)
//    private Order order;
//
//    @OneToMany(mappedBy = "orderItems", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Item> items = new ArrayList<>();



}
