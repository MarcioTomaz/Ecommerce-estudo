package com.ec.ecommercev3.DTO.Product;

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

    private Long stock_quantity_discount;
}
