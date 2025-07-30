package com.ec.ecommercev3.DTO.ProductUpdateAudit;

public record FieldChange(
        String field,
        String oldValue,
        String newValue
) {}