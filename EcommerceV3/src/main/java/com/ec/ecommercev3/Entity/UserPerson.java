package com.ec.ecommercev3.Entity;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonInsertDTO;
import com.ec.ecommercev3.Entity.Enums.UserRole;
import com.ec.ecommercev3.Entity.Enums.UserType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "_user_person")
public class UserPerson extends DomainEntity implements UserDetails {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Person person;

    @Column(name = "email", nullable = false)
    private String email;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    //Spring secutiry refactory
    private UserRole role;

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

    public UserPerson(String email, String encryptedPassword, UserRole role) {
        this.email = email;
        this.password = encryptedPassword;
        this.role = role;
    }

    //Spring security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
