package com.ec.ecommercev3.Service.exceptions;

public class CartAlreadyExistsException extends RuntimeException {
    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
