package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Product.ProductEditDTO;
import com.ec.ecommercev3.DTO.Product.ProductEditStockDTO;
import com.ec.ecommercev3.DTO.Product.ProductInsertDTO;
import com.ec.ecommercev3.Entity.Product;
import com.ec.ecommercev3.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductInsertDTO productInsertDTO) {
        Product result = productService.create( productInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/update/{idProdut}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long idProdut,
                                                 @Valid @RequestBody ProductEditDTO productEditDTO) {

        Product result = productService.update(idProdut, productEditDTO);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/update/stock/{idProdut}")
    public ResponseEntity<Product> updateProductStock(@PathVariable Long idProdut,
                                                 @Valid @RequestBody ProductEditStockDTO productEditStockDTO) {

        Product result = productService.updateStock(idProdut, productEditStockDTO);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{idProdut}")
    public ResponseEntity<Product> deleteProductStock(@PathVariable Long idProdut){

        productService.delete(idProdut);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
