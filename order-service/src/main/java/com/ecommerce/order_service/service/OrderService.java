package com.ecommerce.order_service.service;

import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.config.RabbitMQConfig;
import com.ecommerce.order_service.dto.*;
import com.ecommerce.order_service.exception.ServiceUnavailableException;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderItem;
import com.ecommerce.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final RabbitTemplate rabbitTemplate;

    private OrderItem mapToOrderItem(OrderItemRequest request) {
        return new OrderItem(
                null,
                request.productId(),
                request.quantity(),
                0.0,
                null
        );
    }

    @CircuitBreaker(name = "productServiceBreaker", fallbackMethod = "placeOrderFallback")
    public String placeOrder(OrderRequest request) {
        List<OrderItem> orderItems = request.items()
                .stream().map(this::mapToOrderItem).toList();
        double totalPrice = 0;
        for(OrderItem item : orderItems) {
            ProductResponse productInfo = productClient.getProductById(item.getProductId());
            if(item.getQuantity() > productInfo.stock()) {
                throw new RuntimeException("Stok " + productInfo.name() + " Tidak Mencukupi");
            }
            totalPrice += productInfo.price() * item.getQuantity();
            item.setPriceAtOrder(productInfo.price());
        }
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setTotalPrice(totalPrice);
        order.setOrderItems(orderItems);

        orderItems.forEach(item -> item.setOrder(order));
        orderRepository.save(order);

        for(OrderItem items : orderItems) {
            StockUpdateMessage message = new StockUpdateMessage(
                    items.getProductId(),
                    items.getQuantity()
            );
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    message
            );
        }

        OrderNotificationMessage notificationMessage = new OrderNotificationMessage(
                order.getOrderNumber(),
                "Customer@eexample.com",
                order.getTotalPrice()
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE_NAME,
                RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                notificationMessage
        );
        return "Order Berhasil Dibuat Dengan Nomor: " + order.getOrderNumber();
    }

    public String placeOrderFallback(OrderRequest request, Throwable t) {
        throw new ServiceUnavailableException("Gagal membuat Order: Layanan Produk tidak tersedia/sibuk. " + t.getMessage());
    }
}

