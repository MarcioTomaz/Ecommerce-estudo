package com.ec.ecommercev3.Repository.Jpa;

import com.ec.ecommercev3.DTO.Order.OrderDTO;
import com.ec.ecommercev3.DTO.Order.OrderListAdmDTO;
import com.ec.ecommercev3.DTO.Order.OrderListDTO;
import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>,
        JpaSpecificationExecutor<Order> {

    Page<OrderListDTO> findAllByPersonId(Long person_id, Pageable pageable);

    @Query("SELECT new com.ec.ecommercev3.DTO.Order.OrderDTO(o.person.id, o.billingAddress.id, o.shippingAddress.id, null, o.status) " +
            "FROM Order o WHERE o.person = :person AND o.id = :orderId")
    Optional<OrderDTO> findByIdAndUserPerson(@Param("person") Person person, @Param("orderId") Long orderId);

    @Query("SELECT new com.ec.ecommercev3.DTO.Order.OrderListAdmDTO(o.id, o.status, o.total) " +
            "FROM Order o ")
    Page<OrderListAdmDTO> findAllForApproval(Pageable pageable);

    List<Order> findByStatus(OrderStatus orderStatus);
}


