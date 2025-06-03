package com.ec.ecommercev3.Repository;
import com.ec.ecommercev3.Entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value =
            "UPDATE _product SET stock = stock + :stockQuantityDiscount WHERE id = :productId")
    void updateStock(
            @Param("productId") Long productId,
            @Param("stockQuantityDiscount") Long stockQuantityDiscount);



    @Transactional
    List<Product> findByStockGreaterThanAndActiveTrue(int stock);

    Optional<Product> findByIdAndActiveTrue(Long productId);
}
