package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllCardByPersonId(Long id);
}
