package com.ec.ecommercev3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ec.ecommercev3.Repository.Jpa")
//@EnableMongoRepositories(basePackages = "com.ec.ecommercev3.Repository.mongo")
public class EcommerceV3Application {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceV3Application.class, args);
    }

}
