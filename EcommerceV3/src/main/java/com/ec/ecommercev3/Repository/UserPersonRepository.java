package com.ec.ecommercev3.Repository;

import com.ec.ecommercev3.Entity.UserPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPersonRepository extends JpaRepository<UserPerson, Long> {

    UserPerson findByEmail(String email);
}
