package com.ec.ecommercev3.Service.validation;

import com.ec.ecommercev3.DTO.UserPersonInsertDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Exception.FieldMessage;
import com.ec.ecommercev3.Repository.UserPersonRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

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

        // Coloque aqui seus testes de validação, acrescentando objetos FieldMessage à lista

        UserPerson user = repository.findByEmail(dto.getEmail());

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