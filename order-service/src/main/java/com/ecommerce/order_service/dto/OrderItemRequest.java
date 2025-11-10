package com.ecommerce.order_service.dto;

public record OrderItemRequest(
        String productId,
        int quantity
) {
}
