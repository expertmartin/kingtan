package com.kingtan.store.product.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public record Category(
        @Id String categoryId,
        String name,
        String parentCategoryId
) {
}

