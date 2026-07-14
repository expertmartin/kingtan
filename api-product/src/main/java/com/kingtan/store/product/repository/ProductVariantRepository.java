package com.kingtan.store.product.repository;

import com.kingtan.store.product.model.ProductVariant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductVariantRepository extends MongoRepository<ProductVariant, String> {
    List<ProductVariant> findByProductId(String productId);
}

