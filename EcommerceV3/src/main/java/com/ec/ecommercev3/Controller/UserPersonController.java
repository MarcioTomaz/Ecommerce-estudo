package com.ec.ecommercev3.Controller;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonInsertDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonLoginDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonUpdatePasswordDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Service.UserPersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PutMapping("/update")
    public ResponseEntity<UserPerson> edit(@RequestBody @Valid UserPersonEditDTO userPersonEditDTO,
                                           @AuthenticationPrincipal UserPerson userDetails) {

        UserPerson userPersonEdit = userPersonService.update(userDetails.getId(), userPersonEditDTO);

        return ResponseEntity.status(HttpStatus.OK).body(userPersonEdit);
    }

    @GetMapping("/read")
    public ResponseEntity<List<UserPerson>> readAll(){

        List<UserPerson> userPerson = userPersonService.readAll();

        return ResponseEntity.status(HttpStatus.OK).body(userPerson);
    }

    @GetMapping("/readById")
    public ResponseEntity<UserPerson> readById(@AuthenticationPrincipal UserPerson userDetails){

        UserPerson userPerson = userPersonService.readById(userDetails.getId());

        return ResponseEntity.status(HttpStatus.OK).body(userPerson);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserPerson> deleteById(@PathVariable Long id){
        userPersonService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
