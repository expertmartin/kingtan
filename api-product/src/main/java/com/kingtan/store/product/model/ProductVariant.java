package com.kingtan.store.product.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "product_variants")
public record ProductVariant(
        @Id String sku,
        String productId,
        Map<String, String> attributes, // e.g., { "color": "Blue", "size": "M" }
//        BigDecimal price,
        String imageUrl
) {}
