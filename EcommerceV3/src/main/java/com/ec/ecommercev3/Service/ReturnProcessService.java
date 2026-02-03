package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.Entity.Enums.*;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Repository.Jpa.OrderRepository;
import com.ec.ecommercev3.Service.messaging.NotificationKafkaProducer;
import com.ec.ecommercev3.Service.messaging.OrderKafkaProducer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class ReturnProcessService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;
    @Autowired
    private NotificationKafkaProducer notificationKafkaProducer;

    public Object returnProcess(JsonNode returnProcessFields) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode notificationEventNode = mapper.createObjectNode();

            Long orderId = returnProcessFields.path("orderId").asLong();
            String justification = returnProcessFields.path("justification").asText(null);

            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new IllegalAccessException("Pedido não encontrado: " + orderId));

            // Validar status (só devolve se entregue ou enviado)
            if (!(order.getStatus().equals(OrderStatus.SHIPPED) ||
                    order.getStatus().equals(OrderStatus.DELIVERED))) {
                throw new IllegalStateException("Esse pedido ainda não foi entregue/enviado, portanto não pode ser devolvido");
            }

            // Salvar justificativa da devolução
            if (justification == null || justification.isBlank()) {
                throw new IllegalArgumentException("Justificativa da devolução é obrigatória");
            }

            // Colocar pedido em analise
            order.setStatus(OrderStatus.UNDER_REVIEW);

            orderKafkaProducer.sendOrderStatus(
                    order.getId(),
                    OrderStatus.UNDER_REVIEW,
                    justification,
                    null,
                    AlteredByType.SYSTEM,
                    ExecutionType.AUTOMATIC
                    );

            notificationEventNode.put("userId", order.getPerson().getId());
            notificationEventNode.put("orderId", order.getId());
            notificationEventNode.put("title", "OrderUnderReview");
            notificationEventNode.put("message", "orderUnderReview");
            notificationEventNode.put("type", NotificationType.ORDER_UPDATE.toString());
            notificationEventNode.put("referenceId", order.getId());
            notificationEventNode.put("referenceType", ReferenceType.ORDER.toString());
            notificationEventNode.put("timeStamp", Instant.now().toString().formatted());

            notificationKafkaProducer.sendNotification(notificationEventNode);

            return orderRepository.save(order);

        }catch (Exception e){
            log.error("Erro no processo de devolução: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar devolução", e);
        }
    }
}
