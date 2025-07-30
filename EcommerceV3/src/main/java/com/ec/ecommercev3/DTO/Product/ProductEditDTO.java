package com.ec.ecommercev3.DTO.Product;

import com.ec.ecommercev3.Entity.Enums.ProductCategory;
import com.ec.ecommercev3.Entity.Product.ProductHistory;
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
public class  ProductEditDTO {

    private Long id;

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

    private Integer stock;

    public ProductEditDTO(ProductHistory product) {
        this.id = product.getId();
        this.product_name = product.getProduct_name();
        this.product_description = product.getProduct_description();
        this.product_price = product.getProduct_price();
        this.productCategory = product.getProductCategory();
        this.currencyId = product.getCurrency().getId();
        this.stock = product.getStock();
    }

}
