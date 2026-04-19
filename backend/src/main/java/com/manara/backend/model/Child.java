package com.manara.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manara.backend.validation.IsChild;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Le prénom de l'enfant est obligatoire")
    private String firstName;

    @Column(length = 100, nullable = false)
    @NotBlank(message = "Le nom de l'enfant est obligatoire")
    private String lastName;

    @Column(nullable = false)
    @NotNull(message = "L'année de naissance de l'enfant est obligatoire")
    @PastOrPresent(message = "La date de naissance est invalide")
    @IsChild(message = "L'enfant doit avoir moins de 18 ans")
    private LocalDateTime dateOfBirth;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChildMedical> medicalConditions;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ChildParent> parentLinks;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ActivityRegistration> registrations;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Integer getAge() {
        return LocalDateTime.now().getYear() - dateOfBirth.getYear();
    }
}
