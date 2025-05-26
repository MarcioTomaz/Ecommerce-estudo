package com.ec.ecommercev3.DTO.Filters;

import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductFilters {

    private double price;

    @Enumerated(EnumType.STRING)
    private ProductCategory productCategoryFilter;

    private Double minTotal;

    private Double maxTotal;
}
