package com.ec.ecommercev3.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderExpirationService {

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 300000)
    public void verifyOrderExpiration() {

        orderService.expireOrdersIfNeeded();
    }
}
