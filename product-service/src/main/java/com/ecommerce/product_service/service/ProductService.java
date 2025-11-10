package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.exception.OutOfStockException;
import com.ecommerce.product_service.exception.ProductNotFoundException;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private Product maptoProduct(ProductRequest request) {
        return new Product(null, request.name(), request.description(), request.stock(), request.price());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = maptoProduct(request);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public Page<ProductResponse> getALlProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(this::mapToProductResponse);
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        return mapToProductResponse(product);
    }

    public ProductResponse editProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setStock(request.stock());
        product.setPrice(request.price());
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product Not Found"));
        productRepository.delete(product);
    }

}
