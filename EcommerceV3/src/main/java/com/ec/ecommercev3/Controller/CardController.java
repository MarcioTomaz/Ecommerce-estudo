package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Card.CardDTO;
import com.ec.ecommercev3.Entity.Payment.Card;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/create")
    public ResponseEntity<Card> addCard( @AuthenticationPrincipal UserPerson userPerson,
                                         @Valid @RequestBody CardDTO cardDTO) {
        Card result = cardService.create(userPerson.getId(), cardDTO);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Card> readById(@PathVariable Long id){

        Card result = cardService.readById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/card")
    public ResponseEntity<List<CardDTO>> readAllCardById(@AuthenticationPrincipal UserPerson userPerson){

        List<CardDTO> result = cardService.readAllById(userPerson.getId());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> deleteCardById(@AuthenticationPrincipal UserPerson userPerson, @PathVariable Long id) {

        cardService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
