package com.kingtan.store.product.controller;

import com.kingtan.store.product.model.dto.ProductRequest;
import com.kingtan.store.product.model.dto.ProductResponse;
import com.kingtan.store.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(@RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable String id) {
        return productService.getProduct(id);
    }
}
