package com.ecommerce.order_service.dto;

public record StockUpdateMessage(
        String productId,
        int quantityChange
) {
}
