package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Product.ProductEditDTO;
import com.ec.ecommercev3.DTO.Product.ProductEditStockDTO;
import com.ec.ecommercev3.DTO.Product.ProductInsertDTO;
import com.ec.ecommercev3.Entity.Product;
import com.ec.ecommercev3.Repository.ProductRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    public Product create(ProductInsertDTO productInsertDTO) {
        Product product = modelMapper.map(productInsertDTO, Product.class);

        try {
            productRepository.save(product);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

        return product;
    }

    @Transactional
    public Product update(Long productId, ProductEditDTO productEditDTO){
        Product oldProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        modelMapper.map(productEditDTO, oldProduct);
        oldProduct.setId(productId);

        oldProduct.setUpdatedDate(LocalDateTime.now());

       return productRepository.save(oldProduct);
    }

    @Transactional
    public Product updateStock(Long productId, ProductEditStockDTO productEditStockDTO) {

        Product product;
        // Verifica se o produto existe
        product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        // Atualiza o estoque se o produto existir
        productRepository.updateStock(productId, productEditStockDTO.getStock_quantity_discount());

        // Recarrega o produto do banco de dados para obter os valores atualizados
        product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        // Retorna o produto atualizado
        return product; /// ARRUMAR O RETORNO Q ESTA DESATUALIZADO
    }

    @Transactional
    public void delete(Long productId){

        Product result = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        result.setActive(false);
        productRepository.save(result);
    }

    @Transactional
    public List<Product> readAllProductsForSale() {

        return productRepository.findByStockGreaterThanAndActiveTrue(0);
    }

    @Transactional
    public Product readById(Long productId) {
        Product product;

        product = productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Produto não encontrado!"));

        return product;
    }
}
