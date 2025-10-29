package com.kingtan.store.product.repository.mongo;

import com.kingtan.store.product.model.entity.ProductAttributes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductAttributesRepository extends MongoRepository<ProductAttributes, String> {
}

