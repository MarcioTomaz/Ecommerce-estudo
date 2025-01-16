package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.DTO.PersonDTO;
import com.ec.ecommercev3.Entity.Enums.Gender;
import com.ec.ecommercev3.Entity.Enums.PhoneType;
import com.ec.ecommercev3.Entity.Payment.Card;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table ( name= "_person")
public class Person extends DomainEntity{

    @Column(name = "firstName")
    @NotBlank(message = "Nome invalido!")
    @NotNull(message = "Nome invalido!")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    private String phoneNumber;

    @Column(name = "phoneType")
    @Enumerated( EnumType.STRING)
    private PhoneType phoneType;

    @Column( name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(mappedBy = "person" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addressList;

    @OneToMany(mappedBy = "person" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cardList;

//    @OneToMany(mappedBy = "userPerson", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Order> orders;

    public Person(PersonDTO dto) {
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.birthDate = dto.getBirthDate();
        this.phoneType = dto.getPhoneType();
        this.phoneNumber = dto.getPhoneNumber();
        this.gender = dto.getGender();
    }


}

