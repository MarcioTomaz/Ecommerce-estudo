package com.ec.ecommercev3.Service.messaging;

import com.ec.ecommercev3.DTO.Product.ProductAvailabilityRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAvailabilityRequestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "product.availability.request";
    private final ObjectMapper objectMapper;

    public void sendProductAvailabilityRequest(ProductAvailabilityRequestDTO event, Long userId) {

        try {

            ProductAvailabilityRequestDTO newEvent =
                    new ProductAvailabilityRequestDTO(userId, event.productId(), event.product_name(), event.timestamp());

            String json = objectMapper.writeValueAsString(newEvent);
            kafkaTemplate.send(TOPIC, userId.toString(), json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
