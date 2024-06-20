package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.CartAddDTO;
import com.ec.ecommercev3.Entity.Cart;
import com.ec.ecommercev3.Repository.CartRepository;
import com.ec.ecommercev3.Service.exceptions.CartAlreadyExistsException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public Cart create(CartAddDTO dto) {

        Cart checkExistCart = cartRepository.findByUserPersonId(dto.getUserPersonId());

        if(checkExistCart != null && checkExistCart.isActive()){
            throw new CartAlreadyExistsException("O carrinho já existe!");
        }

        Cart cart = modelMapper.map(dto, Cart.class);

        cart.setItems(dto.getItems());

        //calcular o total do carrinho aqui depois

        try {
            cartRepository.save(cart);
        }catch (Exception e){
            e.printStackTrace();
        }

        return cart;
    }
}
