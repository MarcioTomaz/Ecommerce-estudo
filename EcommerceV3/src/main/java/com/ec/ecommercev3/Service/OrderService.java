package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Address.AddressAdmDTO;
import com.ec.ecommercev3.DTO.Comment.CommentDTO;
import com.ec.ecommercev3.DTO.Filters.OrderFilterDTO;
import com.ec.ecommercev3.DTO.Notification.NotificationEvent;
import com.ec.ecommercev3.DTO.Order.*;
import com.ec.ecommercev3.DTO.Payment.CreditCardPaymentDTO;
import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import com.ec.ecommercev3.DTO.Payment.PixPaymentDTO;
import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.DTO.Product.ProductEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonLOG;
import com.ec.ecommercev3.Entity.*;
import com.ec.ecommercev3.Entity.Comment.Comment;
import com.ec.ecommercev3.Entity.Enums.*;
import com.ec.ecommercev3.Entity.Payment.Card;
import com.ec.ecommercev3.Entity.Payment.CreditCardPaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PixPayment;
import com.ec.ecommercev3.Repository.Jpa.*;
import com.ec.ecommercev3.Repository.Specification.OrderSpecifications;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import com.ec.ecommercev3.Service.exceptions.RoleUnauthorizedException;
import com.ec.ecommercev3.Service.exceptions.TotalMismatchException;
import com.ec.ecommercev3.Service.helper.ItemMapper;
import com.ec.ecommercev3.Service.helper.OrderCalculator;
import com.ec.ecommercev3.Service.helper.StockValidator;
import com.ec.ecommercev3.Service.messaging.NotificationKafkaProducer;
import com.ec.ecommercev3.Service.messaging.OrderKafkaProducer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ec.ecommercev3.Entity.Enums.CommentType.REJECTION_REASON;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    @Autowired
    private NotificationKafkaProducer notificationKafkaProducer;

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private StockValidator stockValidator;

    @Autowired
    private OrderCalculator orderCalculator;

    @Transactional
    public Order create(OrderStepOneDTO order, UserPerson userPerson) {

        Address billingAddress = getActiveAddress(order.billingAddress(), "Endereço de cobrança não encontrado");

        Address shippingAddress = getActiveAddress(order.shippingAddress(), "Endereço de entrega não encontrado");

        Cart cart = cartRepository
                .findByUserPersonId(userPerson.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Carrinho não encontrado"));

        Order newOrder = new Order();
        newOrder.setPerson(userPerson.getPerson());
        newOrder.setBillingAddress(billingAddress);
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setActive(true);
        newOrder.setStatus(OrderStatus.WAITING_FOR_PAYMENT);

        if(cart.getItems().isEmpty()) {
            throw new ResourceNotFoundException("Carrinho vazio!");
        }

        List<Item> orderItems = itemMapper.map(cart.getItems(), newOrder);
        newOrder.setOrderItems(orderItems);

        stockValidator.validateStock(orderItems);
        BigDecimal total = orderCalculator.calculateTotal(orderItems);
        newOrder.setTotal(total.doubleValue());

        orderRepository.save(newOrder);

        // Enviar evento Kafka
        orderKafkaProducer.sendOrderStatus(
                newOrder.getId(),
                newOrder.getStatus(),
                null,
                new UserPersonLOG(
                        userPerson.getEmail(),
                        userPerson.getUsername(),
                        userPerson.getId(),
                        userPerson.getRole()
                ),
                AlteredByType.USER,
                ExecutionType.MANUAL
        );

        return newOrder;
    }

    @Transactional
    public List<PaymentMethod> addPaymentOrder(PaymentDTO paymentDTO) {

        // Processa a lista de PaymentMethodDTO dentro do PaymentDTO
        List<PaymentMethod> savedPayments = paymentDTO.paymentMethods().stream()
                .map(dto -> convertToEntityAndSave(dto, paymentDTO))
                .collect(Collectors.toList());

        checkPaymentBusinessRules(paymentDTO, savedPayments);

        updateOrderStatusToShipping(paymentDTO.id());

        return savedPayments;
    }

    private void checkPaymentBusinessRules(PaymentDTO paymentDTO, List<PaymentMethod> savedPayments) {

        Order orderToCheck = orderRepository.findById(paymentDTO.id()).orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado!"));

        //v1
        savedPayments.stream().filter(payment -> payment.getAmountPaid() <= 0)
                .findFirst().ifPresent(payment -> {
                    throw new IllegalArgumentException("Valor inválido: " + payment.getAmountPaid() + " \nid da transação: " + payment.getTransactionId());
                });

        //v2
        double result = savedPayments.stream().mapToDouble(PaymentMethod::getAmountPaid)
                .sum();

        if (!orderToCheck.getTotal().equals(result)) {
            throw new TotalMismatchException("Os valores de pagamento não estão corretos!");
        }
    }

    private void updateOrderStatusToShipping(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow((() -> new ResourceNotFoundException("Pedido não encontrado! ID: " + orderId)));

        UserPerson userPerson = userPersonRepository.findByPerson(order.getPerson());

        order.setStatus(OrderStatus.PAID);

        orderKafkaProducer.sendOrderStatus(order.getId(), OrderStatus.PAID, null,
                new UserPersonLOG(userPerson.getEmail(), userPerson.getUsername(), userPerson.getId(),
                        userPerson.getRole()), AlteredByType.USER, ExecutionType.MANUAL);

        orderRepository.save(order);
    }

    private PaymentMethod convertToEntityAndSave(PaymentMethodDTO paymentMethodDTO, PaymentDTO paymentDTO) {
        PaymentMethod paymentMethod;

        Order order = orderRepository.findById(paymentDTO.id())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado para o ID: " + paymentDTO.id()));

        if (paymentMethodDTO instanceof PixPaymentDTO pixDTO) {
            PixPayment pixPayment = new PixPayment();
            pixPayment.setPixKey(pixDTO.getPixKey());
            pixPayment.setPixKeyType(pixDTO.getPixKeyType());
            pixPayment.setAmountPaid(pixDTO.getAmountPaid());
            pixPayment.setTransactionId(pixDTO.getTransactionId());
            pixPayment.setOrder(order); // Associa a ordem
            paymentMethod = pixPayment;

        } else if (paymentMethodDTO instanceof CreditCardPaymentDTO cardDTO) {
            CreditCardPaymentMethod creditCardPaymentMethod = new CreditCardPaymentMethod();

            Card paymentCard = cardRepository.findById(cardDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Cartão não encontrado!"));

            creditCardPaymentMethod.setCard(paymentCard);

            creditCardPaymentMethod.setTransactionId(cardDTO.getTransactionId());

            creditCardPaymentMethod.setAmountPaid(cardDTO.getAmountPaid());

            creditCardPaymentMethod.setInstallments(cardDTO.getInstallments());
            creditCardPaymentMethod.setOrder(order); // Associa a ordem

            paymentMethod = creditCardPaymentMethod;

        } else {
            throw new IllegalArgumentException("Tipo de pagamento desconhecido: " + paymentMethodDTO.getType());
        }

        return paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public Page<OrderListDTO> findAllClientOrders(UserPerson userPerson, Pageable pageable, OrderFilterDTO filter) {

        Specification<Order> spec = OrderSpecifications.byFilter(filter);

        Page<Order> result = orderRepository.findAll(spec, pageable);

        return result.map(order -> new OrderListDTO(order.getId(), order.getStatus(), order.getCreationDate(), order.getTotal()));
    }

    @Transactional
    public OrderDTO findOrderById(UserPerson userPerson, Long orderId) {

        Order result = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Pedido não encontrado!"));

        if(!result.getPerson().getId().equals(userPerson.getId()) && !userPerson.getRole().equals(UserRole.ADMIN)) {
            throw new AccessDeniedException("Você não tem permissão para acessar este pedido.");
        }

        List<OrderItemsDTO> orderItemsDTO = result.getOrderItems().stream()
                .map(item -> new OrderItemsDTO(item.getId(),
                        new ProductEditDTO(item.getProduct()),
                        item.getQuantity())).toList();


        PersonDTO personDTO = new PersonDTO(result.getPerson());
        AddressAdmDTO billingAddressDTO = new AddressAdmDTO(result.getBillingAddress());
        AddressAdmDTO shippingAddressDTO = new AddressAdmDTO(result.getShippingAddress());

        List<PaymentMethodDTO> paymentMethods = paymentMethodRepository.findByOrderId(orderId)
                .stream()
                .map(PaymentMethodDTO::fromEntity) // Convertendo para o DTO correto
                .toList();

        List<CommentDTO> commentDTO = result.getComments().stream()
                .map(comment -> new CommentDTO(comment.getComment(), comment.getCommentType()))
                .toList();

        // Retornar o DTO preenchido
        return new OrderDTO(
                personDTO,
                billingAddressDTO,
                shippingAddressDTO,
                orderItemsDTO,
                result.getTotal(),
                result.getStatus(),
                paymentMethods,
                commentDTO
        );
    }

    @Transactional
    public OrderSumaryDTO findOrderByIdSummary(UserPerson userPerson, Long orderId) {

        Order result = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Pedido não encontrado!"));

        List<OrderItemsDTO> orderItemsDTO = result.getOrderItems().stream()
                .map(item -> new OrderItemsDTO(item.getId(),
                        new ProductEditDTO(item.getProduct()),
                        item.getQuantity())).toList();

        // Retornar o DTO preenchido
        return new OrderSumaryDTO(
                orderItemsDTO,
                result.getTotal()
        );
    }

    @Transactional
    public Page<OrderListAdmDTO> findAllOrdersForApprove(UserPerson userPerson, Pageable pageable, OrderFilterDTO filter) {

        boolean isAdmin = userPerson.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new RoleUnauthorizedException("Acesso negado!");
        }

        Specification<Order> spec = OrderSpecifications.byFilter(filter);

        Page<Order> result = orderRepository.findAll(spec, pageable);

        return result.map(order -> new OrderListAdmDTO(order.getId(), order.getStatus(), order.getCreationDate(), order.getTotal()));
    }

    @Transactional
    public OrderAdmDTO findOrderByIdAdm(UserPerson userPerson, Long orderId) {

        Order result = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Pedido não encontrado!"));

        PersonDTO personDTO = new PersonDTO(result.getPerson());
        AddressAdmDTO billingAddressDTO = new AddressAdmDTO(result.getBillingAddress());
        AddressAdmDTO shippingAddressDTO = new AddressAdmDTO(result.getShippingAddress());

        List<PaymentMethodDTO> paymentMethods = paymentMethodRepository.findByOrderId(orderId)
                .stream()
                .map(PaymentMethodDTO::fromEntity) // Convertendo para o DTO correto
                .toList();

        // Retornar o DTO preenchido
        return new OrderAdmDTO(
                personDTO,
                billingAddressDTO,
                shippingAddressDTO,
                result.getTotal(),
                result.getStatus(),
                paymentMethods
        );
    }

    @Transactional
    public void admAcceptOrder(AdmOrderManagementDTO admOrderManagementDTO, UserPerson userPerson) {

        Order orderToAccept = orderRepository.findById(admOrderManagementDTO.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if (admOrderManagementDTO.isAccept().equals(true)) {
            orderToAccept.setStatus(OrderStatus.SHIPPED);

            notificationKafkaProducer.sendNotification(
                    new NotificationEvent(
                            orderToAccept.getPerson().getId(),
                            "Pedido aprovado e enviado!",
                            "O pedido #" + orderToAccept.getId() + " Foi enviado!",
                            false,
                            NotificationType.ORDER_UPDATE,
                            orderToAccept.getId(),
                            ReferenceType.ORDER,
                            Instant.now()
                    )
            );

        } else {
            orderToAccept.setStatus(OrderStatus.CANCELED);

            orderToAccept.getComments().add(
                    new Comment(admOrderManagementDTO.reason(),
                            REJECTION_REASON, orderToAccept,
                            userPerson));

            notificationKafkaProducer.sendNotification(
                    new NotificationEvent(
                            orderToAccept.getPerson().getId(),
                            "Pedido recusado! ",
                            "O pedido #" + orderToAccept.getId() + " Foi cancelado! Motivo: " + admOrderManagementDTO.reason(),
                            false,
                            NotificationType.ORDER_UPDATE,
                            orderToAccept.getId(),
                            ReferenceType.ORDER,
                            Instant.now()
                    )
            );

            restoreStockQuantity(orderToAccept);
        }

        orderRepository.save(orderToAccept);

        orderKafkaProducer.sendOrderStatus(
                orderToAccept.getId(),
                orderToAccept.getStatus(),
                admOrderManagementDTO.reason(),
                new UserPersonLOG(
                        userPerson.getEmail(),
                        userPerson.getUsername(),
                        userPerson.getId(),
                        userPerson.getRole()
                ),
                AlteredByType.USER,
                ExecutionType.MANUAL
        );
    }

    @Transactional
    public void restoreStockQuantity(Order orderToAccept) {

        List<Long> productsIds = orderToAccept.getOrderItems().stream()
                .map(i -> i.getProduct().getId()).toList();

        List<Product> products = productRepository.findAllById(productsIds);

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (Item orderItem : orderToAccept.getOrderItems()) {
            Product product = productMap.get(orderItem.getProduct().getId());
            if (product == null) {
                throw new ResourceNotFoundException(
                        "Produto ID " + orderItem.getProduct().getId() + " não encontrado para restauração");
            }
            Long toRestore = orderItem.getQuantity();

            product.setStock((int) (product.getStock() + toRestore));
        }
        productRepository.saveAll(products);
    }

    @Transactional
    public void expireOrdersIfNeeded() {
        Instant now = Instant.now();

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

                notificationKafkaProducer.sendNotification(
                        new NotificationEvent(
                                order.getPerson().getId(),
                                "Pedido Expirado! ",
                                "O pedido #" + order.getId() + " Foi Expirado! Motivo: Falta de pagamento",
                                false,
                                NotificationType.ORDER_UPDATE,
                                order.getId(),
                                ReferenceType.ORDER,
                                Instant.now()
                        )
                );

                restoreStockQuantity(order);
            }

        });
    }


    private Address getActiveAddress(Long id, String errorMessage) {
        return addressRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessage));
    }


}
