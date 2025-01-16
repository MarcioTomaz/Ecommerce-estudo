package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.Payment.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllCardByPersonIdAndActiveTrue(Long id);

    Optional<Card> findByIdAndActiveTrue(Long id);

}
