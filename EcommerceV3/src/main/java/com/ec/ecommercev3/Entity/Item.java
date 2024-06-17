package com.ec.ecommercev3.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name= "_item")
@Entity
public class Item extends DomainEntity {

    @ManyToOne()
    @JsonIgnore// evita loop infinito
    @JoinColumn(name = "cart")
    private Cart cart;

    @ManyToOne()
    @JoinColumn(name = "product")
    private Product product;

    @Column
    private Long quantity;

    @Column
    private Double total_value;

}