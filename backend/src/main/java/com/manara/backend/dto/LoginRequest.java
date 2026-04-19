package com.manara.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Le courriel est requis")
    @Email(message = "Le format du courriel n'est pas valide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;
}
