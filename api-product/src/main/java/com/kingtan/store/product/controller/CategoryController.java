package com.kingtan.store.product.controller;

import com.kingtan.store.product.model.Category;
import com.kingtan.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(productService.createCategory(category));
    }

    @GetMapping("/parent/{parentCategoryId}")
    public ResponseEntity<List<Category>> getCategoriesByParent(@PathVariable String parentCategoryId) {
        return ResponseEntity.ok(productService.getCategoriesByParent(parentCategoryId));
    }

    @GetMapping("")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(productService.getCategories());
    }
}
