package com.ec.ecommercev3.Entity.Payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("CREDIT_CARD")
public class CreditCardPaymentMethod extends PaymentMethod {

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Column(name = "installments")
    private Integer installments; // Número de parcelas
}
