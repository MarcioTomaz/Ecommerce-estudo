package com.ec.ecommercev3.DTO.UserPerson;

import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.Entity.Enums.UserRole;

public record RegisterDTO(String email, String password, UserRole role, PersonDTO personDTO) {
}
