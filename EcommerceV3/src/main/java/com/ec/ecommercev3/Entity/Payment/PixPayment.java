package com.ec.ecommercev3.Entity.Payment;


import com.ec.ecommercev3.Entity.Enums.PixKeyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "_pixPayment")
@DiscriminatorValue("PIX")
public class PixPayment extends PaymentMethod {

    @Enumerated(EnumType.STRING)
    private PixKeyType pixKeyType;

    @Column(name = "pix_key", nullable = false)
    private String pixKey;

}
