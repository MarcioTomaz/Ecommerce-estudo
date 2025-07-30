package com.ec.ecommercev3.DTO;

import com.ec.ecommercev3.Entity.Enums.Gender;
import com.ec.ecommercev3.Entity.Enums.PhoneType;
import com.ec.ecommercev3.Entity.Person;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    @NotBlank(message = "Nome inválido")
    private String firstName;

    @NotBlank(message = "Sobrenome inválido")
    private String lastName;

    @NotNull(message = "Data de nascimento inválido")
    private LocalDate birthDate;

    private String phoneNumber;

    @Enumerated( EnumType.STRING)
    private PhoneType phoneType;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Genero não pode ser vazio")
    private Gender gender;

    public PersonDTO(Person person) {
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.birthDate = person.getBirthDate();
        this.phoneNumber = person.getPhoneNumber();
        this.phoneType = person.getPhoneType();
        this.gender = person.getGender();
    }

}