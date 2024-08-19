package com.ec.ecommercev3.DTO.Card;

import com.ec.ecommercev3.Entity.Enums.CardFlag;
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


    @Valid
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
}
