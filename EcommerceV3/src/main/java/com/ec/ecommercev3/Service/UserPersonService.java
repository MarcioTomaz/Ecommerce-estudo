package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonInsertDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonUpdatePasswordDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.UserPersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserPersonService {

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserPerson create(UserPersonInsertDTO userPersonInsertDTO) {

        UserPerson userPerson = new UserPerson();

        UserDetails emailVerify = userPersonRepository.findByEmail(userPersonInsertDTO.getEmail());

        if(emailVerify.isEnabled() && userPersonInsertDTO.getEmail().equals(emailVerify.getUsername())) {

            throw new ResourceNotFoundException("Email already exists");
        }

        modelMapper.map(userPersonInsertDTO, userPerson);

        try {
            userPersonRepository.save(userPerson);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return userPerson;
    }

    @Transactional
    public UserPerson update(Long userPersonId, UserPersonEditDTO userPersonEditDTO) {
        UserPerson oldUserPerson = userPersonRepository.findByIdAndActiveTrue(userPersonId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        String encryptedPassword = new BCryptPasswordEncoder().encode(userPersonEditDTO.getPassword());
        userPersonEditDTO.setPassword(encryptedPassword);

        // Mapear os dados do DTO para a entidade existente
        modelMapper.map(userPersonEditDTO, oldUserPerson);

        // Atualizar a data de atualização
        oldUserPerson.setUpdatedDate(LocalDateTime.now());

        // Salvar no repositório
        return userPersonRepository.save(oldUserPerson);
    }


    @Transactional
    public List<UserPerson> readAll() {
        List<UserPerson> userPersonList;
        try {
            userPersonList = userPersonRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userPersonList;
    }

    @Transactional
    public UserPersonEditDTO readById(Long id) {

        UserPerson userPerson = userPersonRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("usuário não encontrado"));

        return new UserPersonEditDTO(userPerson);
    }

    @Transactional
    public void deleteById(Long id) {

        UserPerson result = userPersonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        result.setActive(false);
        userPersonRepository.save(result);
    }

    @Transactional
    public UserPerson updatePassword(UserPersonUpdatePasswordDTO upDTO) {

        UserPerson up = userPersonRepository.findById(upDTO.getIdUP()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!up.getPassword().equals(upDTO.getCurrentPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta!");
        }

        up.setPassword(upDTO.getNewPassword());
        up.setUpdatedDate(LocalDateTime.now());
        return userPersonRepository.save(up);
    }
}
