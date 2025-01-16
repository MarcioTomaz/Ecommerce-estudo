package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
