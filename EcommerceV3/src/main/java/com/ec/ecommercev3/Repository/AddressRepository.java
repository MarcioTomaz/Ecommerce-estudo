package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllAddressByPersonIdAndActiveTrue(Long id);


    Optional<Address> findByIdAndActiveTrue(Long addresId);
}
