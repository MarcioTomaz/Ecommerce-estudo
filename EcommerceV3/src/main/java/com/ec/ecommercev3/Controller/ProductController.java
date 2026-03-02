package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Filters.ProductFilters;
import com.ec.ecommercev3.DTO.Product.*;
import com.ec.ecommercev3.Entity.Product.Product;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") ProductInsertDTO productInsertDTO,
            @RequestPart("file") MultipartFile file) {

        Product result = productService.create(productInsertDTO, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/read")
    public ResponseEntity<Page<ProductListDTO>> readAllProductsForSale(Pageable pageable,
                                                               @ModelAttribute ProductFilters filters) {

        Page<ProductListDTO> result = productService.readAllProductsForSale(pageable, filters);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/{productId}")
    public ResponseEntity<Product> readProductById(@PathVariable Long productId) {

        System.out.println(Thread.currentThread());
        Product result = productService.readById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/update")
    public ResponseEntity<Product> updateProduct(@Valid @RequestBody ProductEditDTO productEditDTO,
                                                 @AuthenticationPrincipal UserPerson userPerson) {

        Product result = productService.update(productEditDTO, userPerson);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadProductImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal UserPerson userPerson
    ) {
        String imagePath = productService.updateImage(id, file, userPerson);
        return ResponseEntity.ok(Map.of("image_path", imagePath));
    }

    @PutMapping("/update/stock/{idProdut}")
    public ResponseEntity<Product> updateProductStock(@PathVariable Long idProdut,
                                                      @Valid @RequestBody ProductEditStockDTO productEditStockDTO,
                                                      @AuthenticationPrincipal UserPerson user) {

        Product result = productService.updateStock(idProdut, productEditStockDTO, user);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{idProdut}")
    public ResponseEntity<Product> deleteProductStock(@PathVariable Long idProdut) {

        productService.delete(idProdut);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/waitlist")
    public void productAvailabilityWaitlist(@AuthenticationPrincipal UserPerson userPerson,
                                            @RequestBody ProductAvailabilityRequestDTO dto) {

        productService.productRequest(userPerson, dto);
    }


}
