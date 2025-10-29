package com.kingtan.store.product.event;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductEvent {
    private String eventType; // e.g., "PRODUCT_CREATED", "PRODUCT_UPDATED"
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Map<String, Object> attributes;
}
