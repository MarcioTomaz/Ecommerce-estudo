package com.ec.ecommercev3.DTO.Order;
import com.ec.ecommercev3.DTO.Payment.PaymentMethodDTO;
import java.util.List;

public record PaymentDTO(Long id, //orderId
                         List<PaymentMethodDTO> paymentMethods

){}
