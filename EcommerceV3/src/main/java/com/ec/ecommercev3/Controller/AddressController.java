package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Address.AddressDTO;
import com.ec.ecommercev3.DTO.Address.AddressEditDTO;
import com.ec.ecommercev3.Entity.Address;
import com.ec.ecommercev3.Service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/create/{idPerson}")
    public ResponseEntity<Address> createAddress(@PathVariable Long idPerson,
                                                 @Valid @RequestBody AddressDTO addressDTO) {
        Address result = addressService.create(idPerson, addressDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressEditDTO addressDTO) {
        Address result = addressService.update(id, addressDTO);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Address> readById(@PathVariable Long id) {

        Address result = addressService.readById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/address/{id}")
    public ResponseEntity<List<Address>> readAllAddressById(@PathVariable Long id) {

        List<Address> result = addressService.readAllById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Address> deleteAddressById(@PathVariable Long id) {

        addressService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
