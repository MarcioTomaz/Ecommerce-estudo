package com.ec.ecommercev3.Service.exceptions;

public class OrderCreationException extends RuntimeException {
    public OrderCreationException(String message) {
        super(message);
    }
}
