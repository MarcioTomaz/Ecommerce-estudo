package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.Address.AddressDTO;
import com.ec.ecommercev3.DTO.Address.AddressEditDTO;
import com.ec.ecommercev3.Entity.Address;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/create")
    public ResponseEntity<Address> createAddress(@AuthenticationPrincipal UserPerson userPerson,
                                                 @Valid @RequestBody AddressDTO addressDTO) {
        Address result = addressService.create(userPerson.getId(), addressDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/update/{addressID}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressID, @AuthenticationPrincipal UserPerson userPerson, @Valid @RequestBody AddressEditDTO addressDTO) {
        Address result = addressService.update(addressID, userPerson, addressDTO);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Address> readById(@PathVariable Long id) {

        Address result = addressService.readById(id);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/read/addresses")
    public ResponseEntity<List<Address>> readAllAddressById(@AuthenticationPrincipal UserPerson userPerson) {

        List<Address> result = addressService.readAllById(userPerson.getId());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Address> deleteAddressById(@PathVariable Long id) {

        addressService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
