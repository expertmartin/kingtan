package com.kingtan.store.product.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "product_attributes")
public class ProductAttributes {
    @Id
    private String productId;
    private Map<String, Object> attributes; // Dynamic attributes (e.g., { "color": "blue", "size": "M" })
}