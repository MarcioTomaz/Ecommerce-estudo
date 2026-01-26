package com.ec.ecommercev3.Entity.Payment;


import com.ec.ecommercev3.DTO.Payment.PixPaymentDTO;
import com.ec.ecommercev3.Entity.Enums.PixKeyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_pixPayment")
@DiscriminatorValue("PIX")
public final class PixPayment extends PaymentMethod {

    @Enumerated(EnumType.STRING)
    private PixKeyType pixKeyType;

    @Column(name = "pix_key", nullable = false)
    private String pixKey;

    public PixPayment(PixPaymentDTO pixDTO) {
        this.pixKeyType = pixDTO.getPixKeyType();
        this.pixKey = pixDTO.getPixKey();
        this.setAmountPaid(pixDTO.getAmountPaid());
        this.setTransactionId(pixDTO.getTransactionId());
    }
}
