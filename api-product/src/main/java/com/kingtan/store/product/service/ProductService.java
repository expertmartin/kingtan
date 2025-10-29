package com.kingtan.store.product.service;

import com.kingtan.store.product.event.KafkaProducer;
import com.kingtan.store.product.event.ProductEvent;
import com.kingtan.store.product.model.dto.ProductRequest;
import com.kingtan.store.product.model.dto.ProductResponse;
import com.kingtan.store.product.model.entity.ProductAttributes;
import com.kingtan.store.product.model.entity.ProductEntity;
import com.kingtan.store.product.repository.mongo.ProductAttributesRepository;
import com.kingtan.store.product.repository.jpa.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductAttributesRepository attributesRepository;
    private final KafkaProducer kafkaProducer;

    public ProductResponse createProduct(ProductRequest request) {
        // Generate product ID
        String productId = UUID.randomUUID().toString();

        // Save to PostgreSQL
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(productId);
        productEntity.setName(request.getName());
        productEntity.setPrice(request.getPrice());
        productEntity.setStock(request.getStock());
        productRepository.save(productEntity);

        // Save to MongoDB
        ProductAttributes attributes = new ProductAttributes();
        attributes.setProductId(productId);
        attributes.setAttributes(request.getAttributes());
        attributesRepository.save(attributes);

        // Publish event
        ProductEvent event = new ProductEvent();
        event.setEventType("PRODUCT_CREATED");
        event.setProductId(productId);
        event.setName(request.getName());
        event.setPrice(request.getPrice());
        event.setStock(request.getStock());
        event.setAttributes(request.getAttributes());
        kafkaProducer.sendProductEvent(event);

        // Return response
        ProductResponse response = new ProductResponse();
        response.setId(productId);
        response.setName(request.getName());
        response.setPrice(request.getPrice());
        response.setStock(request.getStock());
        response.setAttributes(request.getAttributes());
        return response;
    }

    public ProductResponse getProduct(String id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        ProductAttributes attributes = attributesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product attributes not found"));

        ProductResponse response = new ProductResponse();
        response.setId(productEntity.getId());
        response.setName(productEntity.getName());
        response.setPrice(productEntity.getPrice());
        response.setStock(productEntity.getStock());
        response.setAttributes(attributes.getAttributes());
        return response;
    }

    // Add methods for update and delete as needed
}

