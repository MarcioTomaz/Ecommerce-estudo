package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.CartAddDTO;
import com.ec.ecommercev3.Entity.Cart;
import com.ec.ecommercev3.Service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<Cart> addCart(@Valid @RequestBody CartAddDTO cart) {
        Cart result = cartService.create(cart);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
