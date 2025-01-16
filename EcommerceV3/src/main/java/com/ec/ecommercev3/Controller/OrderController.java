package com.ec.ecommercev3.Controller;


import com.ec.ecommercev3.DTO.Order.OrderDTO;
import com.ec.ecommercev3.DTO.Order.PaymentDTO;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<Order> orderStep1(@RequestBody OrderDTO order1,
                                            @AuthenticationPrincipal UserPerson userPerson) {
        Order order = orderService.create(order1, userPerson);

        return ResponseEntity.ok(order);
    }

    @PostMapping("/{orderId}/payment")
    public ResponseEntity <List<PaymentMethod>> orderStep2(@PathVariable Long orderId, @RequestBody PaymentDTO paymentDTO) {


        List<PaymentMethod> result = orderService.addPaymentOrder(paymentDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }







}
