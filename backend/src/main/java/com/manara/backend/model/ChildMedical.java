package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ChildMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Child child;

    @Column(name = "medical_condition", length = 200, nullable = false)
    @NotBlank(message = "La condition médicale de votre enfant est obligatoire")
    private String condition;

    @Column(nullable = false)
    @NotBlank(message = "La sévérité du condition médicale est obligatoire")
    private String severity;

    private String notes;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
