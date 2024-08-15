package com.ec.ecommercev3.DTO.Address;

import com.ec.ecommercev3.Entity.Enums.AddressType;
import com.ec.ecommercev3.Entity.Enums.ResidencyType;
import com.ec.ecommercev3.Entity.Person;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    @Valid
    private Person person;

    @NotBlank(message = "Rua inválida")
    private String street;

    private ResidencyType residencyType;

    private String observation;

    @NotBlank(message = "O número não pode ser vazio")
    private String number;

    private String district; //bairro

    @NotBlank(message = "o CEP não pode ser vázio")
    private String zipCode; //cep

    private String logradouro;

    @NotBlank(message = "Cidade não pode ser vazia")
    private String city;

    private String country;

    private String state;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
}
