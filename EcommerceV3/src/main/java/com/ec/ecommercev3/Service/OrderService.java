package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Address.AddressAdmDTO;
import com.ec.ecommercev3.DTO.Comment.CommentDTO;
import com.ec.ecommercev3.DTO.Filters.OrderFilterDTO;
import com.ec.ecommercev3.DTO.Order.*;
import com.ec.ecommercev3.DTO.Payment.CreditCardPaymentDTO;
import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import com.ec.ecommercev3.DTO.Payment.PixPaymentDTO;
import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.DTO.Product.ProductEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonLOG;
import com.ec.ecommercev3.Entity.*;
import com.ec.ecommercev3.Entity.Comment.Comment;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.Payment.Card;
import com.ec.ecommercev3.Entity.Payment.CreditCardPaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PixPayment;
import com.ec.ecommercev3.Repository.*;
import com.ec.ecommercev3.Repository.Specification.OrderSpecifications;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import com.ec.ecommercev3.Service.exceptions.RoleUnauthorizedException;
import com.ec.ecommercev3.Service.exceptions.TotalMismatchException;
import com.ec.ecommercev3.Service.messaging.OrderKafkaProducer;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ec.ecommercev3.Entity.Enums.CommentType.REJECTION_REASON;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private OrderKafkaProducer orderKafkaProducer;

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order create(OrderStepOneDTO order, UserPerson userPerson) {

        Address billingAddress = addressRepository
                .findByIdAndActiveTrue(order.billingAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Endereço de cobrança não encontrado"));
        Address shippingAddress = addressRepository
                .findByIdAndActiveTrue(order.shippingAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Endereço de entrega não encontrado"));

        Cart cart = cartRepository
                .findByUserPersonId(userPerson.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Carrinho não encontrado"));

        Order newOrder = new Order();
        newOrder.setPerson(userPerson.getPerson());
        newOrder.setBillingAddress(billingAddress);
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setActive(true);
        newOrder.setStatus(OrderStatus.WAITING_FOR_PAYMENT);

        List<Item> orderItems = cart.getItems().stream()
                .map(cartItem -> new Item(
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
                        newOrder
                ))
                .toList();
        newOrder.setOrderItems(orderItems);

        // Bulk‑load dos produtos que serão ajustados no estoque
        List<Long> productIds = orderItems.stream()
                .map(i -> i.getProduct().getId())
                .toList();

        List<Product> products = productRepository.findAllById(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        for (Item orderItem : orderItems) {
            Product product = productMap.get(orderItem.getProduct().getId());
            Long qtyToDiscount = orderItem.getQuantity();

            if (product.getStock() < qtyToDiscount) {
                throw new IllegalArgumentException(
                        "Estoque insuficiente para o produto: " + product.getProduct_name());
            }
            product.setStock((int) (product.getStock() - qtyToDiscount));
        }

        productRepository.saveAll(products);

        // Calcular total do pedido e salvar order
        double total = orderItems.stream()
                .mapToDouble(i -> i.getQuantity() * i.getProduct().getProduct_price())
                .sum();
        BigDecimal roundedTotal = BigDecimal.valueOf(total)
                .setScale(2, RoundingMode.HALF_UP);
        newOrder.setTotal(roundedTotal.doubleValue());

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
                )
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
                new UserPersonLOG(userPerson.getEmail(), userPerson.getUsername(), userPerson.getId(), userPerson.getRole()));

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

        } else {
            orderToAccept.setStatus(OrderStatus.CANCELED);

            orderToAccept.getComments().add(
                    new Comment(admOrderManagementDTO.reason(),
                            REJECTION_REASON, orderToAccept,
                            userPerson));

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
                )
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
}
