package com.ecommerce.product_service.listener;

import com.ecommerce.product_service.dto.StockUpdateMessage;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class StockUpdateListener {
    private final ProductRepository productRepository;

    @RabbitListener(queues = "stock_update_queue")
    @Transactional
    public void handleStockUpdate(StockUpdateMessage message) {
      log.info("Menerima pesan Stock Update product ID: {} dengan Qty: {}",
              message.productId(), message.quantityChange());

        Optional<Product> isProductPresent = productRepository.findById(message.productId());

        if(isProductPresent.isPresent()) {
            Product product = isProductPresent.get();
            int newStock = product.getStock() - message.quantityChange();
            if(newStock >= 0) {
                product.setStock(newStock);
                productRepository.save(product);
                log.info("Stok berhasil diperbarui. Produk: {}, Stok Baru: {}", product.getName(), newStock);
            }
            else {
                log.warn("Gagal memperbarui stok untuk Product ID {}: Stok tidak mencukupi (diperlukan: {}, saat ini: {})",
                        message.productId(), message.quantityChange(), product.getStock());
            }
        }
        else {
            log.error("Product ID {} tidak ditemukan di database.", message.productId());
        }
    }
}
