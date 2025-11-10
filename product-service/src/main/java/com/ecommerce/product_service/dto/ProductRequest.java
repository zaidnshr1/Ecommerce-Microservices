package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductRequest(

        @NotBlank(message = "Nama Produk Wajib Diisi.")
        String name,
        String description,
        @Positive(message = "Tidak Bisa Memasukkan Stock Kurang Dari 0")
        int stock,
        @DecimalMin(value = "1.0", message = "Harga Harus Lebih Dari 0")
        double price
) {
}
