package com.ec.ecommercev3.DTO;

import com.ec.ecommercev3.Entity.DomainEntity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEditStockDTO extends DomainEntity {

    @NotNull
    private Integer stock_quantity_discount;
}
