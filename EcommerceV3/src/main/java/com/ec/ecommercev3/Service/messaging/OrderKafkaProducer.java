package com.ec.ecommercev3.Service.messaging;

import com.ec.ecommercev3.DTO.Order.OrderEvent;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonLOG;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.UserPerson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "order.status";

    public void sendOrderStatus(Long orderId, OrderStatus status, String refuseReason, UserPersonLOG userPersonLOG) {

        try {

            OrderEvent event = new OrderEvent(orderId, status, refuseReason, userPersonLOG, Instant.now());
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, orderId.toString(), json);

        }catch (JsonProcessingException e ){
            throw new RuntimeException("Erro ao enviar evento de pedido: " + e);
        }
    }

}
