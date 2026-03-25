package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entite pour les conditions medicales d'un enfant.
 * Chaque enfant peut avoir plusieurs conditions (allergies, etc.).
 */
@Entity
@Data
public class ChildMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // L'enfant associe a cette condition
    @ManyToOne
    @JoinColumn(nullable = false)
    private Child child;

    // La condition medicale (ex: "Allergie aux arachides")
    @Column(name = "medical_condition", length = 200, nullable = false)
    @NotBlank(message = "La condition médicale de votre enfant est obligatoire")
    private String condition;

    // La severite est optionnelle (le parent n'est pas oblige de la specifier)
    @Column
    private String severity;

    // Notes supplementaires (optionnel)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
