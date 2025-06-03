package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Filters.ProductFilters;
import com.ec.ecommercev3.DTO.Product.*;
import com.ec.ecommercev3.Entity.Product;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.ProductRepository;
import com.ec.ecommercev3.Repository.Specification.ProductSpefications;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import com.ec.ecommercev3.Service.messaging.ProductAvailabilityRequestProducer;
import com.ec.ecommercev3.Service.messaging.ProductStockUpdateProducer;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAvailabilityRequestProducer productAvailabilityRequestProducer;

    @Autowired
    private ProductStockUpdateProducer productStockUpdateProducer;

    public Product create(ProductInsertDTO productInsertDTO) {
        Product product = modelMapper.map(productInsertDTO, Product.class);

        try {
            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Transactional
    public Product update(Long productId, ProductEditDTO productEditDTO) {
        Product oldProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        modelMapper.map(productEditDTO, oldProduct);
        oldProduct.setId(productId);

        oldProduct.setUpdatedDate(LocalDateTime.now());

        return productRepository.save(oldProduct);
    }

    @Transactional
    public Product updateStock(Long productId, ProductEditStockDTO productEditStockDTO, UserPerson user) {

        try {
            Product product;
            product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            if(product.getStock().equals(0)){
                productStockUpdateProducer.productStockUpdate(
                        new ProductStockUpdateDTO(user.getId(), productId, productEditStockDTO.getStock_quantity_discount())
                );
            }

            productRepository.updateStock(productId, productEditStockDTO.getStock_quantity_discount());

            product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));



            return product;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Transactional
    public void delete(Long productId) {

        Product result = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        result.setActive(false);
        productRepository.save(result);
    }

    @Transactional
    public Page<ProductListDTO> readAllProductsForSale(Pageable pageable, ProductFilters filters) {


        Specification<Product> spec = ProductSpefications.byFilter(filters);

        Page<Product> result = productRepository.findAll(spec, pageable);

        return result.map(r -> new ProductListDTO(r.getId(), r.getProduct_name(), r.getProduct_description(),
                r.getProduct_price(), r.getProductCategory(), r.getCurrency(), r.getStock()));
    }


    @Transactional
    public Product readById(Long productId) {
        Product product;

        product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado!"));

        return product;
    }

    @Transactional
    public void productRequest(UserPerson userPerson, ProductAvailabilityRequestDTO dto) {

        try {
            Optional<Product> result = productRepository.findById(dto.productId());

            if (result.isEmpty()) {
                throw new ResourceNotFoundException("Produto não encontrado!");
            }

            productAvailabilityRequestProducer
                    .sendProductAvailabilityRequest(dto);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
