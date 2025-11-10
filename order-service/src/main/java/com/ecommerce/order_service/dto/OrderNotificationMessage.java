package com.ecommerce.order_service.dto;

public record OrderNotificationMessage(
        String orderNumber,
        String customerEmail,
        double totalPrice
) {
}
