package com.ec.ecommercev3.Repository.Specification;

import com.ec.ecommercev3.DTO.Filters.OrderFilterDTO;
import com.ec.ecommercev3.Entity.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecifications {

    public static Specification<Order> byFilter(OrderFilterDTO filter){

        return (root, query, cb) ->{
            List<Predicate> preds = new ArrayList<>();

            if(filter.getStatus() != null){
                preds.add(cb.equal(root.get("status"), filter.getStatus()));
            }

            return cb.and(preds.toArray(new Predicate[0]));
        };
    }
}
