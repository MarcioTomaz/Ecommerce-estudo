package com.ec.ecommercev3.Repository.Jpa;

import com.ec.ecommercev3.Entity.Product.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {


    Optional<ProductHistory> findTopByProductIdOrderByVersionDesc(Long id);
}
