package com.ec.ecommercev3.Repository.Jpa;

import com.ec.ecommercev3.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
