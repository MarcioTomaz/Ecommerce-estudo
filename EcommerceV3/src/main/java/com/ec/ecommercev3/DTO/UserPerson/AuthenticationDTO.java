package com.ec.ecommercev3.DTO.UserPerson;

import com.ec.ecommercev3.Entity.Enums.UserType;

public record AuthenticationDTO(String email, String password, UserType userType) {
}
