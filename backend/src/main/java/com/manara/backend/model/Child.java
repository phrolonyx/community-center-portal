package com.manara.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "L'année de naissance de l'enfant est obligatoire")
    private LocalDateTime dateOfBirth;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    private List<ChildMedical> medicalConditions;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    private List<ChildParent> parentLinks;

    @OneToMany(mappedBy = "child")
    @JsonIgnore
    private List<ActivityRegistration> registrations;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Integer getAge() {
        return LocalDateTime.now().getYear() - dateOfBirth.getYear();
    }
}
