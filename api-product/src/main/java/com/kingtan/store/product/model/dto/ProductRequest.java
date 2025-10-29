package com.kingtan.store.product.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ProductRequest {
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Map<String, Object> attributes;
}
