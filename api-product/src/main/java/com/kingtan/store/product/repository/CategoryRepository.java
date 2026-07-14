package com.kingtan.store.product.repository;

import com.kingtan.store.product.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByParentCategoryId(String parentCategoryId);
}

