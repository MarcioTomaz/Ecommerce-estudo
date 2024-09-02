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

import java.util.List;

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

    @GetMapping("/read")
    public ResponseEntity<List<Product>> readAllProduct() {

        List<Product> result = productService.readAllProductsForSale();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/{productId}")
    public ResponseEntity<Product> readProductById(@PathVariable Long productId) {

        Product result = productService.readById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/update/{idProduct}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long idProduct,
                                                 @Valid @RequestBody ProductEditDTO productEditDTO) {

        Product result = productService.update(idProduct, productEditDTO);

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
