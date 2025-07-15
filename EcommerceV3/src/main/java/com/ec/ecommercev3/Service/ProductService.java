package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Filters.ProductFilters;
import com.ec.ecommercev3.DTO.Product.*;
import com.ec.ecommercev3.DTO.ProductUpdateAudit.FieldChange;
import com.ec.ecommercev3.DTO.ProductUpdateAudit.ProductUpdateAuditDTO;
import com.ec.ecommercev3.DTO.ProductUpdateAudit.UserAuditDTO;
import com.ec.ecommercev3.Entity.Product.Product;
import com.ec.ecommercev3.Entity.Product.ProductHistory;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.Jpa.ProductHistoryRepository;
import com.ec.ecommercev3.Repository.Jpa.ProductRepository;
import com.ec.ecommercev3.Repository.Specification.ProductSpefications;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import com.ec.ecommercev3.Service.messaging.ProductAvailabilityRequestProducer;
import com.ec.ecommercev3.Service.messaging.ProductUpdateHistoryAuditProducer;
import com.ec.ecommercev3.Service.messaging.WaitlistRestockNotifier;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private ProductUpdateHistoryAuditProducer productUpdateHistoryAuditProducer;

    @Autowired
    private WaitlistRestockNotifier waitlistRestockNotifier;
    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    public Product create(ProductInsertDTO productInsertDTO) {

        productInsertDTO.setCurrencyId(1L);
        Product product = modelMapper.map(productInsertDTO, Product.class);

        try {
            productRepository.save(product);

            ProductHistory productHistory = new ProductHistory();
            modelMapper.map(product, productHistory);
            productHistory.setProduct(product);
            productHistory.setVersion(product.getVersion());
            productHistory.setId(null);
            productHistoryRepository.save(productHistory);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Transactional
    public Product update(ProductEditDTO productUpdateDTO, UserPerson userPerson) {

        Product product = productRepository.findById(productUpdateDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        Product oldProduct = new Product(product);

        modelMapper.map(productUpdateDTO, product);

        product.setVersion(product.getVersion() + 1); // Incrementa a versão
        product.setUpdatedDate(LocalDateTime.now());

        Product updatedProduct = productRepository.save(product);

        ProductHistory newVersionHistory = new ProductHistory();
        modelMapper.map(updatedProduct, newVersionHistory);

        newVersionHistory.setId(null);
        newVersionHistory.setProduct(updatedProduct);
        newVersionHistory.setVersion(updatedProduct.getVersion());

        ProductHistory savedHistory = productHistoryRepository.save(newVersionHistory);

        publishProductUpdateAudit(oldProduct, updatedProduct, userPerson);

        return updatedProduct;
    }


    private void publishProductUpdateAudit(Product oldProductSnapshot, Product currentProduct, UserPerson user) {

        List<FieldChange> fieldsChanged = new ArrayList<>();

        if(!oldProductSnapshot.getProduct_name().equals(currentProduct.getProduct_name())) {
            fieldsChanged.add(new FieldChange("productName", oldProductSnapshot.getProduct_name(),
                    currentProduct.getProduct_name()));
        }

        if(!oldProductSnapshot.getProduct_description().equals(currentProduct.getProduct_description())) {
            fieldsChanged.add(new FieldChange("productDescription", oldProductSnapshot.getProduct_description(),
                    currentProduct.getProduct_description()));
        }

        if(!oldProductSnapshot.getProductCategory().equals(currentProduct.getProductCategory())) {
            fieldsChanged.add(new FieldChange("productCategory", oldProductSnapshot.getProductCategory().toString(),
                    currentProduct.getProductCategory().toString()));
        }

        if(oldProductSnapshot.getProduct_price() != currentProduct.getProduct_price()) {
            fieldsChanged.add(new FieldChange("price", String.valueOf(oldProductSnapshot.getProduct_price()),
                    String.valueOf(currentProduct.getProduct_price())));
        }

        if(!fieldsChanged.isEmpty()){
            ProductUpdateAuditDTO productUpdateAuditDTO = new ProductUpdateAuditDTO(currentProduct.getId(),
                    new UserAuditDTO(user.getId(), user.getPerson().getFirstName(), user.getEmail(), user.getRole().toString()),
                    Instant.now(), fieldsChanged);

            productUpdateHistoryAuditProducer.send(productUpdateAuditDTO);
        }
    }


    @Transactional
    public Product updateStock(Long productId, ProductEditStockDTO productEditStockDTO, UserPerson user) {

        try {
            Product product;
            product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            if (product.getStock().equals(0)) {
                waitlistRestockNotifier.waitlistRestockNotifier(
                        new ProductStockUpdateDTO(user.getId(), productId, product.getVersion(), productEditStockDTO.quantity())
                );
            }

            productRepository.updateStock(productId, productEditStockDTO.quantity());

            product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            return product;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void delete(Long productId) {

//        Product result = productRepository.findByIdAndActiveTrue(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        Product result = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        result.setActive(false);
        productRepository.save(result);
    }

    @Transactional
    public Page<ProductListDTO> readAllProductsForSale(Pageable pageable, ProductFilters filters) {

        Specification<Product> spec = ProductSpefications.byFilter(filters);

        Page<Product> result = productRepository.findAll(spec, pageable);

        return result.map(r -> new ProductListDTO(r.getId(), r.getProduct_name(), r.getProduct_description(),
                r.getProduct_price(), r.getProductCategory(), r.getCurrency(), r.getStock(), r.getVersion()));
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
            Optional<Product> result = productRepository.findById(userPerson.getId());

            if (result.isEmpty()) {
                throw new ResourceNotFoundException("Produto não encontrado!");
            }

            productAvailabilityRequestProducer
                    .sendProductAvailabilityRequest(dto, userPerson.getId());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
