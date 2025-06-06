package com.ec.ecommercev3.DTO.Product;

import com.ec.ecommercev3.Entity.Currency;
import com.ec.ecommercev3.Entity.DomainEntity;
import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductEditDTO extends DomainEntity {

    @NotBlank
    private String product_name;

    private String product_description;

    @NotNull
    private Double product_price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProductCategory productCategory;

    @NotNull
    private Long currencyId;

    @NotNull
    private Integer stock;
}
