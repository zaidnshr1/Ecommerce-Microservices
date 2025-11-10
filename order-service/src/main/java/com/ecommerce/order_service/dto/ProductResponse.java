package com.ecommerce.order_service.dto;

public record ProductResponse(
        String id,
        String name,
        String description,
        int stock,
        double price
) {
}
