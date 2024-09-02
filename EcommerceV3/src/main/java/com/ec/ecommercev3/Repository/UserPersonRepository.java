package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.UserPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPersonRepository extends JpaRepository<UserPerson, Long> {

    UserDetails findByEmail(String email);

//    Optional<UserPerson> findByEmail(String email);

    Optional<UserPerson> findByPassword(String password);


}
