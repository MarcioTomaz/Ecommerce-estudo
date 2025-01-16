package com.ec.ecommercev3.DTO.Payment;


import com.ec.ecommercev3.Entity.Enums.PaymentType;
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
}
