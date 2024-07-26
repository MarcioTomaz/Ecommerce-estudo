package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonInsertDTO;
import com.ec.ecommercev3.Entity.Enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "_user_person")
public class UserPerson extends DomainEntity{

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Person person;

    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public UserPerson(UserPersonInsertDTO dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.userType = dto.getUserType();
        this.person = new Person(dto.getPersonDTO());
    }

    public UserPerson(UserPersonEditDTO dto) {
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.userType = dto.getUserType();
        this.person = new Person(dto.getPersonDTO());
    }
}
