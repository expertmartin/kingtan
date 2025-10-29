package com.kingtan.store.product.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    @Value("${spring.kafka.topic:product-events}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductEvent(ProductEvent event) {
        kafkaTemplate.send(topic, event.getProductId(), event);
    }
}
