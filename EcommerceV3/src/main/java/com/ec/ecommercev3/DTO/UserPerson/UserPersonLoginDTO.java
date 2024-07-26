package com.ec.ecommercev3.DTO.UserPerson;

import com.ec.ecommercev3.Service.validation.UserPersonInsertValid;
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
public class UserPersonLoginDTO {

    private Long id;

    @NotBlank
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha inválida")
    private String password;
}
