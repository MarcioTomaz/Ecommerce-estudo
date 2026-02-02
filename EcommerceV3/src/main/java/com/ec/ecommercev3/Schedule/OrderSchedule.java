package com.ec.ecommercev3.Schedule;

import com.ec.ecommercev3.Service.OrderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderSchedule {

    @Autowired
    private OrderManagementService orderManagementService;

    @Scheduled(fixedRate = 600000) // 10 minutos em milissegundos
    public void processShippedOrders(){

        orderManagementService.processShippedOrders();
    }

    @Scheduled(fixedRate = 300000)
    public void verifyOrderExpiration() {

        orderManagementService.expireOrdersIfNeeded();
    }
}