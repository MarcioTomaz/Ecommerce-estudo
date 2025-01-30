package com.ec.ecommercev3.DTO.Payment;

import com.ec.ecommercev3.Entity.Enums.PaymentType;
import com.ec.ecommercev3.Entity.Enums.PixKeyType;
import com.ec.ecommercev3.Entity.Payment.PixPayment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PixPaymentDTO extends PaymentMethodDTO {

    private PixKeyType pixKeyType;
    private String pixKey;
    private Double amountPaid;
    private String transactionId;

    public PixPaymentDTO(PixPayment paymentMethod) {
        super();

    }

    @Override
    public PaymentType getType() {
        return PaymentType.PIX;
    }
}