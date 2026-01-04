package com.ec.ecommercev3.Entity.Payment;

import com.ec.ecommercev3.Entity.DomainEntity;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.UserPerson;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "_paymentMethod")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class PaymentMethod extends DomainEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    private Double amountPaid;

    private String transactionId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_person_id")
    private UserPerson userPerson;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Relacionamento com Order
}
