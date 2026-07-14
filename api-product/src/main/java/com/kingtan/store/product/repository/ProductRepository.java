package com.kingtan.store.product.repository;

import com.kingtan.store.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByStatus(Product.Status status);
}

