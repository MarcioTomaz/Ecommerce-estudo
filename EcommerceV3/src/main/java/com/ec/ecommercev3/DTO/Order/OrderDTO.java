package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.DTO.Address.AddressAdmDTO;
import com.ec.ecommercev3.DTO.Comment.CommentDTO;
import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.util.List;


public record OrderDTO(
        PersonDTO person,
        AddressAdmDTO billingAddress,
        AddressAdmDTO shippingAddress,
        List<OrderItemsDTO> orderItemsDTO,
        Double cart,
//        String coupon,
        OrderStatus status, //pendente, enviado, etc...
        List<PaymentMethodDTO> paymentMethods,
        List<CommentDTO> comments
) {
}