package com.ec.ecommercev3.DTO.UserPerson;

import com.ec.ecommercev3.Entity.Enums.UserRole;

public record UserPersonLOG(String email, String name, Long Id, UserRole userRole) {
}
