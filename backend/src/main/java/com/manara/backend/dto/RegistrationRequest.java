package com.manara.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationRequest {

    @NotNull(message = "L'id de l'enfant ne peut pas etre null ni invalide")
    @Positive(message = "L'id de l'enfant doit etre positive")
    private Integer childId;

    @NotNull(message = "L'id de la seance d'activite ne peut pas etre null ni invalide")
    @Positive(message = "L'id de la seance doit etre positive")
    private Integer activitySessionId;

}
