package com.ec.ecommercev3.Service.messaging;


import com.ec.ecommercev3.DTO.Product.ProductAvailabilityRequestDTO;
import com.ec.ecommercev3.DTO.Product.ProductStockUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductStockUpdateProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "product.stock.update";
    private final ObjectMapper objectMapper;

    public void productStockUpdate(ProductStockUpdateDTO event) {

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event.productId().toString(), json);

        }catch (Exception e) {
            return;
        }
    }

}
