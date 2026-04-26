package com.manara.backend.dto;

import com.manara.backend.model.Child;
import com.manara.backend.validation.IsChild;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChildRequest {

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotNull(message = "La date de naissance est obligatoire")
    @PastOrPresent(message = "La date de naissance est invalide")
    @IsChild(message = "L'enfant doit avoir moins de 18 ans")
    private LocalDateTime dateOfBirth;

    @NotNull
    private String relationship; // Père, Mère, Tuteur, etc.

    private String medical;

    @NotNull
    private Integer parentId;

    public Child toChild() {
        Child child = new Child();
        child.setFirstName(this.firstName);
        child.setLastName(this.lastName);
        child.setDateOfBirth(this.dateOfBirth);
        child.setUpdatedAt(LocalDateTime.now());
        return child;
    }
}
