package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Le nom du lieu est obligatoire")
    private String name;

    @NotBlank(message = "L'adresse du lieu ne doit pas être vide")
    private String address;

    @Positive(message = "Le lieu doit avoir un capacité positif")
    private Integer capacity;

    private String description;
}
