package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Card.CardDTO;
import com.ec.ecommercev3.Entity.Card;
import com.ec.ecommercev3.Service.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/create/{idPerson}")
    public ResponseEntity<Card> addCard( @PathVariable Long idPerson, @Valid @RequestBody CardDTO cardDTO) {
        Card result = cardService.create(idPerson, cardDTO);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Card> readById(@PathVariable Long id){

        Card result = cardService.readById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/card/{id}")
    public ResponseEntity<List<Card>> readAllAddressById(@PathVariable Long id){

        List<Card> result = cardService.readAllById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> deleteAddressById(@PathVariable Long id) {

        cardService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
