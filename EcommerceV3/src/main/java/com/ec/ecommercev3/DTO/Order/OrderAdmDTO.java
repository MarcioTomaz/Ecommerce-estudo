package com.ec.ecommercev3.DTO.Order;

import com.ec.ecommercev3.DTO.Address.AddressAdmDTO;
import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.util.List;

public record OrderAdmDTO(
        PersonDTO person,
        AddressAdmDTO billingAddress,
        AddressAdmDTO shippingAddress,
        Double total,
//        String coupon,
        OrderStatus status, //pendente, enviado, etc...,
        List<PaymentMethodDTO> paymentMethods
) {
}