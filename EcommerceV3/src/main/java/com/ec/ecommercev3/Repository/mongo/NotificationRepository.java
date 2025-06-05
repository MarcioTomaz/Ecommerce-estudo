package com.ec.ecommercev3.Repository.mongo;

import com.ec.ecommercev3.Entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {

    @Query(value = "{ 'userId' : :#{#userId} }", sort = "{ 'timestamp' : -1 }")
    List<Notification> findByUserId(Long userId);

}
