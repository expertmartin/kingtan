package com.kingtan.store.product.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @Column
    private String id; // UUID or string ID
    @Column
    private String name;
    @Column
    private BigDecimal price;
    @Column
    private Integer stock;
}
