package com.kingtan.store.product.controller;

import com.kingtan.store.product.model.Product;
import com.kingtan.store.product.model.ProductVariant;
import com.kingtan.store.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(productId, product));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }
    @GetMapping("/")
    public ResponseEntity<List<Product>> getProduct() {
        return ResponseEntity.ok(productService.getProduct());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ProductVariant> createVariant(@PathVariable String productId, @RequestBody ProductVariant variant) {
        return ResponseEntity.ok(productService.createVariant(variant));
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductVariant>> getVariantsByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getVariantsByProduct(productId));
    }
}