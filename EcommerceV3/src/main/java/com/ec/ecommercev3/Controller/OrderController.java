package com.ec.ecommercev3.Controller;


import com.ec.ecommercev3.DTO.Filters.OrderFilterDTO;
import com.ec.ecommercev3.DTO.Order.*;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Order> orderStep1(@RequestBody OrderStepOneDTO order1,
                                            @AuthenticationPrincipal UserPerson userPerson) {
        Order order = orderService.create(order1, userPerson);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity<List<PaymentMethod>> orderStep2(@RequestBody PaymentDTO paymentDTO) {

        orderService.addPaymentOrder(paymentDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<OrderListDTO>> orderList(@AuthenticationPrincipal UserPerson userPerson,
                                                        Pageable pageable,
                                                        @ModelAttribute OrderFilterDTO filter) {

        Page<OrderListDTO> result = orderService.findAllClientOrders(userPerson, pageable, filter);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> orderStep3(@AuthenticationPrincipal UserPerson userPerson, @PathVariable Long orderId) {

        OrderDTO result = orderService.findOrderById(userPerson, orderId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/details/{orderId}")
    public ResponseEntity<OrderDTO> orderDetails(@AuthenticationPrincipal UserPerson userPerson, @PathVariable Long orderId) {

        OrderDTO result = orderService.findOrderById(userPerson, orderId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/details/summary/{orderId}")
    public ResponseEntity<OrderSumaryDTO> orderDetailsSummary(@AuthenticationPrincipal UserPerson userPerson, @PathVariable Long orderId) {

        OrderSumaryDTO result = orderService.findOrderByIdSummary(userPerson, orderId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
