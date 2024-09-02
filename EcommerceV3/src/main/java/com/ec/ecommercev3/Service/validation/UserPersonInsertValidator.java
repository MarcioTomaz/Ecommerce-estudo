package com.ec.ecommercev3.Service.validation;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonInsertDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Exception.FieldMessage;
import com.ec.ecommercev3.Repository.UserPersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class UserPersonInsertValidator implements ConstraintValidator<UserPersonInsertValid, UserPersonInsertDTO> {

    @Autowired
    private UserPersonRepository repository;

    @Override
    public void initialize(UserPersonInsertValid ann) {
    }

    @Override
    public boolean isValid(UserPersonInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

//        UserPerson user = repository.findByEmail(dto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Email não encontrado!"));
        UserDetails user = repository.findByEmail(dto.getEmail());

        if (user != null) {
            list.add(new FieldMessage("email", "Email já cadastrado!"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}