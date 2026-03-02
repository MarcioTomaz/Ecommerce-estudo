package com.ec.ecommercev3.Service.Workers;

import com.ec.ecommercev3.Entity.Enums.AlteredByType;
import com.ec.ecommercev3.Entity.Enums.ExecutionType;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Repository.Jpa.OrderRepository;
import com.ec.ecommercev3.Service.OrderService;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import com.ec.ecommercev3.Service.messaging.OrderKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderExpirationWorker {

    private final OrderRepository orderRepository;
    private final OrderKafkaProducer orderKafkaProducer;
    private final OrderService orderService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expireIfNeeded(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado!" + orderId));

        if(!order.getStatus().equals(OrderStatus.WAITING_FOR_PAYMENT)){
            return;
        }
        Duration time = Duration.between(order.getCreationDate(), LocalDateTime.now());
        if(time.toMinutes() < 30){
            return;
        }

        order.setStatus(OrderStatus.EXPIRED);
        orderRepository.save(order);

        orderKafkaProducer.sendOrderStatus(
                order.getId(), OrderStatus.EXPIRED, "expired.payment",
                null, AlteredByType.SYSTEM, ExecutionType.AUTOMATIC
        );

        orderService.restoreStockQuantity(order);
    }
}
