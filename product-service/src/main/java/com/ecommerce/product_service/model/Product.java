package com.ecommerce.product_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private int stock;
    private double price;

}
