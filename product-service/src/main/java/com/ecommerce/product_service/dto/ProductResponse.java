package com.ecommerce.product_service.dto;

import lombok.Builder;

@Builder
public record ProductResponse(
        String id,
        String name,
        String description,
        int stock,
        double price
) {
}
