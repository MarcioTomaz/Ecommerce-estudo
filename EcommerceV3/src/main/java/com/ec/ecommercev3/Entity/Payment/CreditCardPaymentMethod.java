package com.ec.ecommercev3.Entity.Payment;

import com.ec.ecommercev3.DTO.Payment.CreditCardPaymentDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("CREDIT_CARD")
public final class CreditCardPaymentMethod extends PaymentMethod {

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @Column(name = "installments")
    private Integer installments; // Número de parcelas

    public CreditCardPaymentMethod(CreditCardPaymentDTO cardDTO, Card card) {
        this.card = card;
        this.installments = cardDTO.getInstallments();
        this.setAmountPaid(cardDTO.getAmountPaid());
        this.setTransactionId(cardDTO.getTransactionId());
    }
}
