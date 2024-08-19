package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.Address.AddressDTO;
import com.ec.ecommercev3.DTO.Address.AddressEditDTO;
import com.ec.ecommercev3.Entity.Address;
import com.ec.ecommercev3.Entity.Person;
import com.ec.ecommercev3.Repository.AddressRepository;
import com.ec.ecommercev3.Repository.PersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Address create(Long id, AddressDTO addressDTO) {
        Person person = null;

        person = personRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Usuário não encontrado") );

        addressDTO.setPerson(person);

        Address address = new Address(addressDTO);

        addressRepository.save(address);

        return address;
    }

    @Transactional
    public Address update(Long id, AddressEditDTO addressDTO) {
        Address oldAddress = addressRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Endereço não encontrado!"));

        Person person = null;

        person = personRepository.findById(addressDTO.getPerson().getId()).orElseThrow( () -> new ResourceNotFoundException("Usuário não encontrado") );

        addressDTO.setPerson(person);

        modelMapper.map(addressDTO, oldAddress);

        oldAddress.setUpdatedDate(LocalDateTime.now());

        return addressRepository.save(oldAddress);
    }

    @Transactional
    public Address readById(Long addresId) {
        Address address = null;

        address = addressRepository.findById(addresId)
                .orElseThrow( () -> new ResourceNotFoundException("Endereço não encontrado"));

        return address;
    }

    @Transactional
    public List<Address> readAllById(Long clientId) {
        List<Address> addresses = null;

        addresses = addressRepository.findAllAddressByPersonId(clientId);

        return addresses;
    }

    @Transactional
    public void deleteById(Long id) {

        Address address = addressRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Endereço não encontrado!"));

        addressRepository.delete(address);
    }
}
