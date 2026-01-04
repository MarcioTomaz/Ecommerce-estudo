package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Repository.Jpa.OrderRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReturnProcessService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    public Object returnProcess(JsonNode returnProcessFields) {

        try {

            JsonNode dados = returnProcessFields.path("order");
            Long orderId = dados.path("orderId").asLong();
            String justification = dados.path("justification").asText(null);

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


        }catch (Exception e){
            log.error("Erro no processo de devolução: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar devolução", e);
        }
        
            return null;
    }
}
