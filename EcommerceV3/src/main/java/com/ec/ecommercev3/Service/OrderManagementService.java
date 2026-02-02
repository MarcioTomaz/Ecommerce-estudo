package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.Entity.Comment.Comment;
import com.ec.ecommercev3.Entity.Enums.AlteredByType;
import com.ec.ecommercev3.Entity.Enums.ExecutionType;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Repository.Jpa.OrderRepository;
import com.ec.ecommercev3.Service.messaging.OrderKafkaProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.ec.ecommercev3.Entity.Enums.CommentType.REJECTION_REASON;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderManagementService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    @Transactional
    public void expireOrdersIfNeeded() {

        List<Order> orders = orderRepository.findByStatus(OrderStatus.WAITING_FOR_PAYMENT);

        orders.forEach(order -> {

            Duration time = Duration.between(order.getCreationDate(), LocalDateTime.now());

            if (time.toMinutes() >= 30) {
                log.info("Expirando pedido {} após {} minutos", order.getId(), time.toMinutes());
                order.setStatus(OrderStatus.EXPIRED);

                order.getComments().add(
                        new Comment("Pedido Expirado por falta de pagamento",
                                REJECTION_REASON, order, null
                        ));

                orderRepository.save(order);
                orderKafkaProducer.sendOrderStatus(
                        order.getId(),
                        OrderStatus.EXPIRED,
                        "Expired due to payment not received in time",
                        null,
                        AlteredByType.SYSTEM,
                        ExecutionType.AUTOMATIC
                );

                orderService.restoreStockQuantity(order);
            }

        });
    }

    @Transactional
    public void processShippedOrders() {
        try {
            List<Order> orders = orderRepository.findByStatus(OrderStatus.SHIPPED);
            orders.forEach(order -> {
                order.setStatus(OrderStatus.DELIVERED);
                orderRepository.save(order);
                log.info("Pedido {} marcado como entregue", order.getId());

                orderKafkaProducer.sendOrderStatus(
                        order.getId(),
                        OrderStatus.DELIVERED,
                        null,
                        null,
                        AlteredByType.SYSTEM,
                        ExecutionType.AUTOMATIC);
            });

        }catch (Exception e){
            log.error("Erro ao processar pedidos enviados: {}", e.getMessage());
        }
    }
}
