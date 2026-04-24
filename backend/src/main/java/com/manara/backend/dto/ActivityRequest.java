package com.manara.backend.dto;

import com.manara.backend.model.Activity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ActivityRequest {

    @NotBlank(message = "Le nom de l'activité est requis")
    private String name;

    private String description;

    public Activity toActivity() {
        Activity activity = new Activity();
        activity.setName(this.name);
        activity.setDescription(this.description);
        return activity;
    }
}
