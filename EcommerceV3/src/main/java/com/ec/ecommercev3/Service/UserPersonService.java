package com.ec.ecommercev3.Service;

import com.ec.ecommercev3.DTO.UserPerson.UserPersonEditDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonInsertDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonLoginDTO;
import com.ec.ecommercev3.DTO.UserPerson.UserPersonUpdatePasswordDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.UserPersonRepository;
import com.ec.ecommercev3.Service.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserPersonService {

    @Autowired
    private UserPersonRepository userPersonRepository;

    @Autowired
    private ModelMapper modelMapper;

    public UserPerson create(UserPersonInsertDTO userPersonInsertDTO) {

        UserPerson userPerson = new UserPerson();

        Optional<UserPerson> emailVerify = userPersonRepository.findByEmail(userPersonInsertDTO.getEmail());

        if(emailVerify.isPresent() && userPersonInsertDTO.getEmail().equals(emailVerify.get().getEmail())) {

            throw new ResourceNotFoundException("Email already exists");
        }

        modelMapper.map(userPersonInsertDTO, userPerson);

        try {
            userPersonRepository.save(userPerson);
        } catch (Exception ex) {

        }

        return userPerson;
    }

    @Transactional
    public UserPerson update(Long userPersonId, UserPersonEditDTO userPersonEditDTO) {
        UserPerson oldUserPerson = userPersonRepository.findById(userPersonId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Mapear os dados do DTO para a entidade existente
        modelMapper.map(userPersonEditDTO, oldUserPerson);

        // Atualizar a data de atualização
        oldUserPerson.setUpdatedDate(LocalDateTime.now());

        // Salvar no repositório
        return userPersonRepository.save(oldUserPerson);
    }


    @Transactional
    public List<UserPerson> readAll() {
        List<UserPerson> userPersonList = new ArrayList<>();
        try {
            userPersonList = userPersonRepository.findAll();
        } catch (Exception e) {

        }

        return userPersonList;
    }

    @Transactional
    public UserPerson readById(Long id) {
        UserPerson userPerson = null;

        userPerson = userPersonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("usuário não encontrado"));

        return userPerson;
    }

    @Transactional
    public void deleteById(Long id) {

        UserPerson result = userPersonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        userPersonRepository.delete(result);
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

    @Transactional
    public UserPersonLoginDTO login(UserPersonLoginDTO userPersonLoginDTO) {
        UserPerson user = userPersonRepository.findByEmail(userPersonLoginDTO.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email incorreto!"));

        if (!user.getPassword().equals(userPersonLoginDTO.getPassword())) {
            throw new ResourceNotFoundException("Senha incorreta!");
        }
        UserPersonLoginDTO result = modelMapper.map(user, UserPersonLoginDTO.class);
        result.setId(user.getId());
        return result;
    }
}
