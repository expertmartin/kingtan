package com.kingtan.store.product.service;

import com.kingtan.store.product.model.Category;
import com.kingtan.store.product.model.Product;
import com.kingtan.store.product.model.ProductVariant;
import com.kingtan.store.product.repository.CategoryRepository;
import com.kingtan.store.product.repository.ProductRepository;
import com.kingtan.store.product.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVariantRepository variantRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackCreateProduct")
    public Product createProduct(Product product) {
        // Validate category exists
        categoryRepository.findById(product.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product saved = productRepository.save(product);
        kafkaTemplate.send("product-created", saved);

        return saved;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackUpdateProduct")
    public Product updateProduct(String productId, Product dto) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Validate category exists
        categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product updated = new Product(
                productId,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.categoryId(),
                dto.brand(),
                dto.status(),
                existing.createdAt(),
                Instant.now()
        );

        Product saved = productRepository.save(updated);
        kafkaTemplate.send("product-updated", saved);

        return saved;
    }

    public void deleteProduct(String productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.deleteById(productId);
        kafkaTemplate.send("product-deleted", new Product(productId, null, null, null, null, null, null, null, null));
    }

    public List<Product> getProduct() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    public Product getProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return product;
    }
    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackCreateVariant")
    public ProductVariant createVariant(ProductVariant dto) {
        // Validate product exists
        productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = new ProductVariant(
                UUID.randomUUID().toString(),
                dto.productId(),
                dto.attributes(),
//                dto.price(),
                dto.imageUrl()
        );
        ProductVariant saved = variantRepository.save(variant);
        kafkaTemplate.send("variant-created", saved);

        return saved;
    }

    public List<ProductVariant> getVariantsByProduct(String productId) {
        return variantRepository.findByProductId(productId);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackCreateCategory")
    public Category createCategory(Category dto) {
        // Validate parent category if provided
        if (dto.parentCategoryId() != null) {// && !dto.parentCategoryId().isBlank()) {
            categoryRepository.findById(dto.parentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }

        Category category = new Category(
//                UUID.randomUUID().toString(),
                dto.categoryId(),
                dto.name(),
                dto.parentCategoryId()
        );
        Category saved = categoryRepository.save(category);
        kafkaTemplate.send("category-created", saved);

        return saved;
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByParent(String parentCategoryId) {
        return categoryRepository.findByParentCategoryId(parentCategoryId);
    }
    // Fallback methods
    public Product fallbackCreateProduct(Product dto, Throwable t) {
        // Log error and return null
        return null;
    }

    public Product fallbackUpdateProduct(String productId, Product dto, Throwable t) {
        // Log error and return null
        return null;
    }

    public ProductVariant fallbackCreateVariant(ProductVariant dto, Throwable t) {
        // Log error and return null
        return null;
    }

    public Category fallbackCreateCategory(Category dto, Throwable t) {
        // Log error and return null
        return null;
    }
}




