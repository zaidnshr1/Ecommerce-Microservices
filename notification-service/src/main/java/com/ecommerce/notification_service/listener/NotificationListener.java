package com.ecommerce.notification_service.listener;

import com.ecommerce.notification_service.dto.OrderNotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @RabbitListener(queues = "email_notification_queue")
    public void handleOrderNotification(OrderNotificationMessage message) {
        log.info("Menerima Order Notification: Order #{} dengan total Rp {}",
                message.orderNumber(), message.totalPrice());

        log.info("Status: Email Order berhasil dikirim ke {}", message.customerEmail());
    }

}
