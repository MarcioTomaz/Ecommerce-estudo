package com.ec.ecommercev3.DTO.Order;


import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;

import java.util.List;

public record PaymentDTO(Long id,
                         OrderStatus orderStatus,
                         List<PaymentMethodDTO> paymentMethods

){}
