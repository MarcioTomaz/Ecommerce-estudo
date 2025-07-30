package com.ec.ecommercev3.DTO.Filters;

import com.ec.ecommercev3.Entity.Enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderFilterDTO {

    private OrderStatus status;
    private LocalDate startDate;
    private Double minTotal;
    private Double maxTotal;

}
