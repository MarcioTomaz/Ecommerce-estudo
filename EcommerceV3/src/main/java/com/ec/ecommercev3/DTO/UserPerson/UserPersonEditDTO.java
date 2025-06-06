package com.ec.ecommercev3.DTO.UserPerson;

import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.Entity.Enums.UserType;
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
public class UserPersonEditDTO {

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