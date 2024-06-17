package com.ec.ecommercev3.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPersonUpdatePasswordDTO {

    @NotNull
    private Long idUP;

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
