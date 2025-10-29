package com.kingtan.store.product.repository.jpa;

import com.kingtan.store.product.model.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testSaveProduct() {
        ProductEntity product = new ProductEntity();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test Product");
        product.setPrice(new BigDecimal("49.99"));
        product.setStock(10);
        productRepository.save(product);
    }
}