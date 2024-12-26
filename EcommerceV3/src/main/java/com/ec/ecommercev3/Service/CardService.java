package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Card.CardDTO;
import com.ec.ecommercev3.Entity.Card;
import com.ec.ecommercev3.Entity.Person;
import com.ec.ecommercev3.Repository.CardRepository;
import com.ec.ecommercev3.Repository.PersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Card create(Long id, CardDTO cardDTO) {
        Person person;

        person = personRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Usuário não encontrado") );

        cardDTO.setPerson(person);

        Card card = new Card(cardDTO);

        cardRepository.save(card);

        return card;
    }

    @Transactional
    public Card readById(Long cardId) {
        Card card;

        card = cardRepository.findByIdAndActiveTrue(cardId)
                .orElseThrow( () -> new ResourceNotFoundException("Cartão não encontrado"));

        return card;
    }

    @Transactional
    public List<Card> readAllById(Long clientId) {
        List<Card> card;

        card = cardRepository.findAllCardByPersonIdAndActiveTrue(clientId);

        return card;
    }

    @Transactional
    public void deleteById(Long id) {

        Card card = cardRepository.findByIdAndActiveTrue(id)
                .orElseThrow( () -> new ResourceNotFoundException("Cartão não encontrado!"));

        card.setActive(false);
        cardRepository.save(card);
    }
}
