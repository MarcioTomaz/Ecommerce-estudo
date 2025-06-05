package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.CartAddDTO;
import com.ec.ecommercev3.DTO.Checkout.ProductCheckoutDTO;
import com.ec.ecommercev3.Entity.Cart;
import com.ec.ecommercev3.Entity.Item;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.Jpa.CartRepository;
import com.ec.ecommercev3.Repository.Jpa.UserPersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Transactional
    public Cart create(CartAddDTO dto, Long personId) {

        UserPerson userPerson = userPersonRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado!"));

        Cart checkExistCart = cartRepository.findByUserPersonId(personId).orElse(null);

        Cart cart;
        if (checkExistCart != null) {
            cart = checkExistCart;
            cart.getItems().clear(); // Limpa itens antigos
        } else {
            cart = new Cart();
            cart.setUserPerson(userPerson);
        }

        for (Item item : dto.getItems()) {
            item.setCart(cart);  // Configura a referência reversa
            cart.getItems().add(item);  // Adiciona à lista existente
        }

        return cartRepository.save(cart);  // Persiste tudo de uma vez
    }

    @Transactional
    public List<ProductCheckoutDTO> findItemFromCart(UserPerson userPerson) {

        try {

            return cartRepository.findItemFromCart(userPerson.getId());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
