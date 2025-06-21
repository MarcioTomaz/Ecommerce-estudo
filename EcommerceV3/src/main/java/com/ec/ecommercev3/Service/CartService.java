package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.CartAddDTO;
import com.ec.ecommercev3.DTO.Checkout.ProductCheckoutDTO;
import com.ec.ecommercev3.Entity.Cart;
import com.ec.ecommercev3.Entity.Item;
import com.ec.ecommercev3.Entity.Product.Product;
import com.ec.ecommercev3.Entity.Product.ProductHistory;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.Jpa.CartRepository;
import com.ec.ecommercev3.Repository.Jpa.ProductHistoryRepository;
import com.ec.ecommercev3.Repository.Jpa.ProductRepository;
import com.ec.ecommercev3.Repository.Jpa.UserPersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    @Transactional
    public Cart create(CartAddDTO dto, Long personId) {

        List<Long> productsIds = dto.getItems().stream().map(item -> item.getProduct().getId()).toList();

        List<Product> productsStock = productRepository.findAllById(productsIds);

        Map<Long, ProductHistory> historyMap = productsIds.stream()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> productHistoryRepository.findTopByProductIdOrderByVersionDesc(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Histórico não encontrado para produto ID: " + id))
                ));

        Map<Long, Product> productMap = productsStock.stream().collect(Collectors.toMap(Product::getId, Function.identity()));

        dto.getItems().forEach(item -> {
            Product product = productMap.get(item.getProduct().getId());

            if(product == null) {
                throw new ResourceNotFoundException("Produto não encontrado: ID " + item.getProduct().getId());
            }

            if(product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Estoque insuficiente para o produto: "
                        + product.getProduct_name());
            }
        });

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

            ProductHistory history = historyMap.get(item.getProduct().getId());
            item.setProductHistory(history);

            cart.getItems().add(item);  // Adiciona à lista existente
        }

        return cartRepository.save(cart);
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
