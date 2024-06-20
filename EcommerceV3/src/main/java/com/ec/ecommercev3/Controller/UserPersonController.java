package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.UserPersonEditDTO;
import com.ec.ecommercev3.DTO.UserPersonInsertDTO;
import com.ec.ecommercev3.DTO.UserPersonUpdatePasswordDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.UserPersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/userPerson")
public class UserPersonController {

    @Autowired
    private UserPersonService userPersonService;


    @PutMapping("/update/password")
    private ResponseEntity<UserPerson> updatePassword(@RequestBody @Valid UserPersonUpdatePasswordDTO userPersonUpdatePasswordDTO){

        UserPerson result = userPersonService.updatePassword(userPersonUpdatePasswordDTO);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<UserPerson> create(@RequestBody UserPersonInsertDTO userPersonInsertDTO){

        UserPerson result = userPersonService.create(userPersonInsertDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserPerson> edit(@PathVariable Long id,
                                           @RequestBody UserPersonEditDTO userPersonEditDTO) {

        UserPerson userPersonEdit = userPersonService.update(id, userPersonEditDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userPersonEdit);
    }

    @GetMapping("/read")
    public ResponseEntity<List<UserPerson>> readAll(){

        List<UserPerson> userPerson = userPersonService.readAll();

        return ResponseEntity.status(HttpStatus.OK).body(userPerson);
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<UserPerson> readById(@PathVariable Long id){

        UserPerson userPerson = userPersonService.readById(id);

        return ResponseEntity.status(HttpStatus.OK).body(userPerson);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserPerson> deleteById(@PathVariable Long id){
        userPersonService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
