package com.ec.ecommercev3.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name= "_cart")
@Entity
public class Cart extends DomainEntity {

    @ManyToOne(optional = true)
    @JsonIgnore// evita loop infinito
    @JoinColumn(name = "userPerson")
    private UserPerson userPerson;

    @OneToMany(mappedBy = "cart")
    private List<Item> items;

    @Column
    private boolean currentCart = true;

    @Column
    private Double total_value;
}