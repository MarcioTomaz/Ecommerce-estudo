package com.ec.ecommercev3.Repository.mongo;

import com.ec.ecommercev3.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, Long> {

    Page<Notification> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    Notification findById(String id);
}
