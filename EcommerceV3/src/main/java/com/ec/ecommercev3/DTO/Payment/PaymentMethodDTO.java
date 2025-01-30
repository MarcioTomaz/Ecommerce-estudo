package com.ec.ecommercev3.DTO.Payment;


import com.ec.ecommercev3.Entity.Enums.PaymentType;
import com.ec.ecommercev3.Entity.Payment.CreditCardPaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import com.ec.ecommercev3.Entity.Payment.PixPayment;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PixPaymentDTO.class, name = "PIX"),
        @JsonSubTypes.Type(value = CreditCardPaymentDTO.class, name = "CREDIT_CARD")
})
public abstract class PaymentMethodDTO {
    public abstract PaymentType getType();


    public static PaymentMethodDTO fromEntity(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("O método de pagamento não pode ser nulo.");
        }

        // Verificando o tipo do método de pagamento e convertendo para o DTO adequado
        if (paymentMethod instanceof PixPayment) {
            return new PixPaymentDTO((PixPayment) paymentMethod);
        } else if (paymentMethod instanceof CreditCardPaymentMethod) {
            return new CreditCardPaymentDTO((CreditCardPaymentMethod) paymentMethod);
        }

        throw new IllegalArgumentException("Tipo de pagamento desconhecido: " + paymentMethod.getClass().getSimpleName());
    }
}
