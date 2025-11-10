package com.ecommerce.notification_service.dto;

public record OrderNotificationMessage(
        String orderNumber,
        String customerEmail,
        double totalPrice
) {
}
