package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUserPersonId(Long id);
}
