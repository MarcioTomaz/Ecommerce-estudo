package com.ec.ecommercev3.Service.messaging;

import com.ec.ecommercev3.DTO.ProductUpdateAudit.ProductUpdateAuditDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductUpdateHistoryAuditProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "product.update.history.audit";
    private final ObjectMapper objectMapper;

    public void send(ProductUpdateAuditDTO event) {

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
