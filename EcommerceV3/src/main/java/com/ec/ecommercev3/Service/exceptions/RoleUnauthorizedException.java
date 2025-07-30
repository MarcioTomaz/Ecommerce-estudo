package com.ec.ecommercev3.Service.exceptions;

public class RoleUnauthorizedException extends RuntimeException {
    public RoleUnauthorizedException(String message) {
        super(message);
    }
}
