package com.kingtan.store.product.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document(collection = "products")
public record Product(
        @Id String productId,
        String name,
        String description,
        BigDecimal price,
        String categoryId,
        String brand,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Status status, // Enum: ACTIVE, DISCONTINUED
        Instant createdAt,
        Instant updatedAt
) {
    public enum Status {
        ACTIVE, DISCONTINUED
    }
}

