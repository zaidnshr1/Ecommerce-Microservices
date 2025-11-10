package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductResponse getProductById(@PathVariable("id") String id);

    @PutMapping("/products/stock/{id}")
    void updateProductStock(
            @PathVariable("id") String id,
            @RequestParam("quantityChange") int quantityChange
    );
}
