package com.manara.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityRequest {

    @NotBlank(message = "Le nom de l'activité est requis")
    private String name;

    private String description;
}
