package com.ec.ecommercev3.DTO.Card;

import com.ec.ecommercev3.Entity.Enums.CardFlag;
import com.ec.ecommercev3.Entity.Payment.Card;
import com.ec.ecommercev3.Entity.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {

    private Long id;

    @Valid
    @JsonIgnore
    private Person person;

    private String number;

    private String holder;// titular do cartao

    private LocalDateTime expirationDate; // data de validade

    private String security; // 3 digitos la

    private String holderCpf;

    private Boolean preferencial;

    @Enumerated(EnumType.STRING)
    private CardFlag flag;// bandeira

    private String alias;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.person = card.getPerson();
        this.number = card.getNumber();
        this.holder = card.getHolder();
        this.expirationDate = card.getExpirationDate();
        this.security = card.getSecurity();
        this.holderCpf = card.getHolderCpf();
        this.preferencial = card.getPreferencial();
        this.flag = card.getFlag();
        this.alias = card.getAlias();
    }
}
