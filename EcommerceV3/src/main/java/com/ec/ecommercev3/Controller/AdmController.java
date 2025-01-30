package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Order.AdmOrderManagementDTO;
import com.ec.ecommercev3.DTO.Order.OrderAdmDTO;
import com.ec.ecommercev3.DTO.Order.OrderDTO;
import com.ec.ecommercev3.DTO.Order.OrderListAdmDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/adm")
public class AdmController {


    @Autowired
    private OrderService orderService;

    @GetMapping("/order/list")
    public ResponseEntity<Page<OrderListAdmDTO>> orderAdmList(@AuthenticationPrincipal UserPerson userPerson,
                                                              Pageable pageable) {
        Page<OrderListAdmDTO> result = orderService.findAllOrdersForApprove(userPerson, pageable);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderAdmDTO> openOrderAdm(@AuthenticationPrincipal UserPerson userPerson,
                                                    @PathVariable Long orderId) {

        OrderAdmDTO result = orderService.findOrderByIdAdm(userPerson, orderId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/order/accept")
    public ResponseEntity<String> acceptOrder(@AuthenticationPrincipal UserPerson userPerson,
                                              @RequestBody AdmOrderManagementDTO admOrderManagementDTO) {

        orderService.admAcceptOrder(admOrderManagementDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
