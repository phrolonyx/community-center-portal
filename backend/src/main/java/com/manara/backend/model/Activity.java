package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Le nom de l'activité est requis")
    private String name;

    private String description;

    private String activityImagePath;

    @OneToMany(mappedBy = "activity")
    private List<ActivitySession> sessions;
}
