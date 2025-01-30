package com.ec.ecommercev3.DTO.Payment;

import com.ec.ecommercev3.DTO.Card.CardDTO;
import com.ec.ecommercev3.Entity.Enums.PaymentType;
import com.ec.ecommercev3.Entity.Payment.CreditCardPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

// Subclasse para Cartão de Crédito
public class CreditCardPaymentDTO extends PaymentMethodDTO {

    private Integer installments;
    private Long id;
    private Double amountPaid;
    private String transactionId;

    public CreditCardPaymentDTO(CreditCardPaymentMethod paymentMethod) {
        super();
    }

    @Override
    public PaymentType getType() {
        return PaymentType.CREDIT_CARD;
    }
}