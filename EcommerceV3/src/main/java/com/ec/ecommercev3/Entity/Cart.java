package com.ec.ecommercev3.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_cart")
@Entity
public class Cart extends DomainEntity{

    @ManyToOne(optional = true)
    @JoinColumn(name = "userPerson_id")
    private UserPerson userPerson;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Item> items = new ArrayList<>();

    @Column
    private boolean currentCart;

    @Column
    private Double total_value;

    public void setItems(List<Item> items) {
        this.items = items;
        for (Item item : items) {
            item.setCart(this);
        }
    }

    public void addItem(Item item) {
        items.add(item);
        item.setCart(this);
    }
}
