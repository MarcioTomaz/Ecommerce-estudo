package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.Payment.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    List<PaymentMethod> findByOrderId(Long id);
}
