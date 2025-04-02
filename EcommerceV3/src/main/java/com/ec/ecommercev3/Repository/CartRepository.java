package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.DTO.Checkout.ProductCheckoutDTO;
import com.ec.ecommercev3.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

   Optional<Cart> findByUserPersonId(Long id);

   @Query("SELECT new com.ec.ecommercev3.DTO.Checkout.ProductCheckoutDTO" +
           "(c.id, i.quantity, p.product_name, p.product_price) FROM Item i " +
           " JOIN Cart c ON i.cart.id = c.id " +
           " JOIN Product p ON i.product.id = p.id " +
           " WHERE c.userPerson.id = :personID ")
    List<ProductCheckoutDTO> findItemFromCart(@Param("personID") Long personID);
}
