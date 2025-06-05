package com.ec.ecommercev3.Controller;


import com.ec.ecommercev3.Config.security.TokenService;
import com.ec.ecommercev3.DTO.UserPerson.AuthenticationDTO;
import com.ec.ecommercev3.DTO.UserPerson.LoginResponseDTO;
import com.ec.ecommercev3.DTO.UserPerson.RegisterDTO;
import com.ec.ecommercev3.Entity.UserPerson;
import com.ec.ecommercev3.Repository.Jpa.UserPersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserPersonRepository personRepository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO dto) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(userNamePassword);

        var token = tokenService.generateToken((UserPerson) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token,((UserPerson) auth.getPrincipal()).getRole().getRole() ));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO dto) {

        if(this.personRepository.findByEmail(dto.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        UserPerson newUser = new UserPerson(dto.email(), encryptedPassword, dto.role(), dto.personDTO());

        this.personRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
