package com.ec.ecommercev3.Service.messaging;


import com.ec.ecommercev3.DTO.Notification.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "notification.topic";
    private final ObjectMapper objectMapper;

    public void sendNotification(NotificationEvent event){

        try {
            String json = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(TOPIC, event.userId().toString(), json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao enviar notificacao", e);
        }

    }

}
