package com.ec.ecommercev3.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "_currency")
public class Currency extends DomainEntity {

    private String code;  // Ex: "USD", "BRL"

    private String name;  // Ex: "Dólar Americano", "Real Brasileiro"

    private String symbol; // Ex: "$", "R$"
}
