package com.ec.ecommercev3.Entity.Enums;

import lombok.Getter;

@Getter
public enum OrderStatus {

    PENDING("Pendente"),
    PROCESSING("Em Processamento"),
    SHIPPED("Enviado"),
    DELIVERED("Entregue"),
    CANCELED("Cancelado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

}