package com.ecommerce.product_service.dto;

public record StockUpdateMessage(
        String productId,
        int quantityChange
) {
}
