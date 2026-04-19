package com.manara.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * DTO (Data Transfer Object) pour le formulaire d'inscription.
 * Contient les validations pour chaque champ du formulaire.
 * Les champs d'urgence sont optionnels.
 */

@Data
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Le prénom est requis")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le courriel est requis")
    @Email(message = "Le format du courriel n'est pas valide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotBlank(message = "La confirmation du mot de passe est requise")
    private String confirmPassword;

    @NotBlank(message = "Le téléphone est requis")
    private String telephone;

    @NotBlank(message = "L'adresse est requise")
    @Size(min = 2, message = "L'adresse doit contenir au moins 2 caractères")
    private String adresse;

    @NotBlank(message = "Le code postal est requis")
    @Size(min = 6, max = 7, message = "Format attendu : H2X 1Y4")
    private String postalCode;

    @NotBlank(message = "La ville est requise")
    @Size(min = 2, message = "La ville doit contenir au moins 2 caractères")
    private String ville;

    @NotBlank(message = "La province est requise")
    @Size(min = 2, message = "La province doit contenir au moins 2 caractères")
    private String province;

    @NotBlank(message = "Le rôle est requis")
    private String role;

    // Champs optionnels — contact d'urgence
    private String urgenceNom;
    private String urgenceRelation;
    private String urgenceTel;
}
