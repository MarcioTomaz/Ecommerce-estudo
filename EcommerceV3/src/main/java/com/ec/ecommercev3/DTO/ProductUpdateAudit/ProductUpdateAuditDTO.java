package com.ec.ecommercev3.DTO.ProductUpdateAudit;

import java.time.Instant;
import java.util.List;

public record ProductUpdateAuditDTO(
        Long productId,
        UserAuditDTO updatedBy,
        Instant updatedAt,
        List<FieldChange> changes) {
}
