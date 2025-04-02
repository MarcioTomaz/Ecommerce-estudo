package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Address.AddressAdmDTO;
import com.ec.ecommercev3.DTO.Order.*;
import com.ec.ecommercev3.DTO.Payment.CreditCardPaymentDTO;
import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import com.ec.ecommercev3.DTO.Payment.PixPaymentDTO;
import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.DTO.Product.ProductEditDTO;
import com.ec.ecommercev3.Entity.*;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.Payment.Card;
import com.ec.ecommercev3.Entity.Payment.CreditCardPaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PixPayment;
import com.ec.ecommercev3.Repository.*;
import com.ec.ecommercev3.Service.exceptions.OrderCreationException;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import com.ec.ecommercev3.Service.exceptions.RoleUnauthorizedException;
import com.ec.ecommercev3.Service.exceptions.TotalMismatchException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public Order create(OrderStepOneDTO order, UserPerson userPerson) {

        Order newOrder = new Order();

        Address billingAddress = addressRepository.findByIdAndActiveTrue(order.billingAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Endereço não encontrado"));

        Address shippingAddress = addressRepository.findByIdAndActiveTrue(order.shippingAddress())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Endereço não encontrado"));

        Cart cart = cartRepository.findByUserPersonId(userPerson.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Erro! Carrinho não encontrado"));

        //calculando total do pedido
        Double total = cart.getItems()
                .stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getProduct_price())
                .sum();

        BigDecimal roundedTotal = BigDecimal.valueOf(total).setScale(2, RoundingMode.HALF_UP);
        double roundedTotalDouble = roundedTotal.doubleValue();

        newOrder.setPerson(userPerson.getPerson());
        newOrder.setActive(true);
        newOrder.setBillingAddress(billingAddress);
        newOrder.setShippingAddress(shippingAddress);


        List<Item> orderItems = cart.getItems().stream()
                .map(cartItem -> new Item(
                        cartItem.getProduct(),
                        cartItem.getQuantity(),
//                        cartItem.getTotal_value(),
                        newOrder // Associação com o pedido atual
                ))
                .toList(); // Cria a nova lista de itens

        // Define a nova lista no pedido
        newOrder.setOrderItems(orderItems);

        newOrder.setTotal(roundedTotalDouble);
        newOrder.setStatus(OrderStatus.WAITING_FOR_PAYMENT);

        try {
            orderRepository.save(newOrder);
        } catch (Exception e) {
            throw new OrderCreationException("Erro ao criar o pedido!");
        }

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

        order.setStatus(OrderStatus.PAID);

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
    public Page<OrderListDTO> findAllClientOrders(UserPerson userPerson, Pageable pageable) {

        Page<OrderListDTO> orderListDTO = orderRepository
                .findAllByPersonId(userPerson.getId(), pageable);

        return orderListDTO;
    }

    @Transactional
    public OrderDTO findOrderById(UserPerson userPerson, Long orderId) {

        Order result = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Pedido não encontrado!"));

        List<OrderItemsDTO> orderItemsDTO = result.getOrderItems().stream()
                .map( item -> new OrderItemsDTO(item.getId(),
                        new ProductEditDTO(item.getProduct()),
                        item.getQuantity()) ).toList();


        PersonDTO personDTO = new PersonDTO(result.getPerson());
        AddressAdmDTO billingAddressDTO = new AddressAdmDTO(result.getBillingAddress());
        AddressAdmDTO shippingAddressDTO = new AddressAdmDTO(result.getShippingAddress());

        List<PaymentMethodDTO> paymentMethods = paymentMethodRepository.findByOrderId(orderId)
                .stream()
                .map(PaymentMethodDTO::fromEntity) // Convertendo para o DTO correto
                .toList();

        // Retornar o DTO preenchido
        return new OrderDTO(
                personDTO,
                billingAddressDTO,
                shippingAddressDTO,
                orderItemsDTO,
                result.getTotal(),
                result.getStatus(),
                paymentMethods
        );
    }

    @Transactional
    public OrderSumaryDTO findOrderByIdSummary(UserPerson userPerson, Long orderId) {

        Order result = orderRepository.findById(orderId).orElseThrow(() ->
                new ResourceNotFoundException("Pedido não encontrado!"));

        List<OrderItemsDTO> orderItemsDTO = result.getOrderItems().stream()
                .map( item -> new OrderItemsDTO(item.getId(),
                        new ProductEditDTO(item.getProduct()),
                        item.getQuantity()) ).toList();

        // Retornar o DTO preenchido
        return new OrderSumaryDTO(
                orderItemsDTO,
                result.getTotal()
        );
    }

    @Transactional
    public Page<OrderListAdmDTO> findAllOrdersForApprove(UserPerson userPerson, Pageable pageable) {

        boolean isAdmin = userPerson.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new RoleUnauthorizedException("Acesso negado!");
        }

        Page<Order> result = orderRepository.findAll(pageable);

        return result.map(order -> new OrderListAdmDTO(order.getId(), order.getStatus(), order.getTotal()));
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
    public void admAcceptOrder(AdmOrderManagementDTO admOrderManagementDTO) {

        Order orderToAccept = orderRepository.findById(admOrderManagementDTO.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        if(admOrderManagementDTO.isAccept().equals(true)){
            orderToAccept.setStatus(OrderStatus.SHIPPED);
        }else{
            orderToAccept.setStatus(OrderStatus.CANCELED);
        }

        orderRepository.save(orderToAccept);

    }
}
