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

    public Activity toActivity(ActivityRequest activityRequest) {
        Activity activity = new Activity();
        activity.setName(activityRequest.getName());
        activity.setDescription(activityRequest.getDescription());
        return activity;
    }
}
