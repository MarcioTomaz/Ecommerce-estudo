package com.ec.ecommercev3.DTO;

import com.ec.ecommercev3.Entity.Enums.UserType;
import com.ec.ecommercev3.Service.validation.UserPersonInsertValid;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@UserPersonInsertValid
public class UserPersonInsertDTO {

    @NotBlank
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha inválida")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Valid
    private PersonDTO personDTO;
}