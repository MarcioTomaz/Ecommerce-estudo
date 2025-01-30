package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.DTO.Order.OrderDTO;
import com.ec.ecommercev3.DTO.Order.OrderListAdmDTO;
import com.ec.ecommercev3.DTO.Order.OrderListDTO;
import com.ec.ecommercev3.Entity.Order;
import com.ec.ecommercev3.Entity.Person;
import com.ec.ecommercev3.Entity.UserPerson;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<OrderListDTO> findAllByPersonId(Long person_id, Pageable pageable);

//    OrderDTO findByIdAndUserPerson(UserPerson userPerson, Long orderId);

    @Query("SELECT new com.ec.ecommercev3.DTO.Order.OrderDTO(o.person.id, o.billingAddress.id, o.shippingAddress.id, null, o.status) " +
            "FROM Order o WHERE o.person = :person AND o.id = :orderId")
    Optional<OrderDTO> findByIdAndUserPerson(@Param("person") Person person, @Param("orderId") Long orderId);

    @Query("SELECT new com.ec.ecommercev3.DTO.Order.OrderListAdmDTO(o.id, o.status, o.total) " +
            "FROM Order o ")
    Page<OrderListAdmDTO> findAllForApproval(Pageable pageable);


}


