package com.ec.ecommercev3.Entity;


import com.ec.ecommercev3.DTO.Card.CardDTO;
import com.ec.ecommercev3.Entity.Enums.CardFlag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table( name= "_card")
@Entity
public class Card extends DomainEntity{

    @ManyToOne(optional = true)
    @JsonIgnore// evita loop infinito
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "number")
    private String number;

    @Column(name = "holder")
    private String holder;// titular do cartao

    @Column(name = "expirationDate")
    private LocalDateTime expirationDate; // data de validade

    @Column(name = "security")
    private String security; // 3 digitos la

    @Column(name = "holderCpf")
    private String holderCpf;

    @Column(name = "preferencial")
    private Boolean preferencial;

    @Column(name = "flag")
    @Enumerated( EnumType.STRING)
    private CardFlag flag;// bandeira

    @Column(name = "alias")
    private String alias;

    public Card(CardDTO cardDTO) {
        this.person = cardDTO.getPerson();
        this.number = cardDTO.getNumber();
        this.holder = cardDTO.getHolder();
        this.expirationDate = cardDTO.getExpirationDate();
        this.security = cardDTO.getSecurity();
        this.holderCpf = cardDTO.getHolderCpf();
        this.preferencial = cardDTO.getPreferencial();
        this.flag = cardDTO.getFlag();
        this.alias = cardDTO.getAlias();
    }
}
