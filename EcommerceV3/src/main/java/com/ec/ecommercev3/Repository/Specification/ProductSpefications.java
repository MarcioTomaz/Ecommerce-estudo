package com.ec.ecommercev3.Repository.Specification;

import com.ec.ecommercev3.DTO.Filters.ProductFilters;
import com.ec.ecommercev3.Entity.Product.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpefications {


    public static Specification<Product> byFilter(ProductFilters filter) {

        return ( root, query, cb) ->{
            List<Predicate> preds = new ArrayList<>();

            if(filter.getProductCategoryFilter() != null){
                preds.add(cb.equal(root.get("productCategory"), filter.getProductCategoryFilter()));
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

}
