package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.CartAddDTO;
import com.ec.ecommercev3.DTO.Checkout.ProductCheckoutDTO;
import com.ec.ecommercev3.Entity.Cart;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/create")
    public ResponseEntity<Cart> addCart(@AuthenticationPrincipal UserPerson userPerson,
            @Valid @RequestBody CartAddDTO cart) {
        cartService.create(cart, userPerson.getId());

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/checkoutItems")
    public ResponseEntity<List<ProductCheckoutDTO>> getCart(@AuthenticationPrincipal UserPerson userPerson) {

        List<ProductCheckoutDTO> result = cartService.findItemFromCart(userPerson);

        return ResponseEntity.ok(result);

    }
}
