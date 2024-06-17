package com.ec.ecommercev3.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name= "_cart")
@Entity
public class Cart extends DomainEntity {

    @ManyToOne(optional = true)
//    @JsonIgnore// evita loop infinito
    @JsonBackReference
    @JoinColumn(name = "userPerson")
    private UserPerson userPerson;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;

    @Column
    private boolean currentCart = true;

    @Column
    private Double total_value;
}