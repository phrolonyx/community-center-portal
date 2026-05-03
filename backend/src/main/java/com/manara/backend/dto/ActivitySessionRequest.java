package com.manara.backend.dto;

import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Location;
import com.manara.backend.model.User;
import com.manara.backend.validation.ValidAgeActivitySession;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ValidAgeActivitySession
public class ActivitySessionRequest {

    @NotNull
    private Integer activityId;

    @NotNull
    private Integer hostId;

    @NotNull
    private Integer locationId;

    @Positive
    private Integer capacity;

    @NotNull
    private LocalDateTime dateTimeStart;

    @NotNull
    private LocalDateTime dateTimeEnd;

    @Positive
    @NotNull
    private Integer minAge;

    @Positive
    @NotNull
    private Integer maxAge;

    public ActivitySession toActivitySession(){
        ActivitySession activitySession = new ActivitySession();
        activitySession.setDateTimeStart(this.dateTimeStart);
        activitySession.setDateTimeEnd(this.dateTimeEnd);
        activitySession.setMinAge(this.minAge);
        activitySession.setMaxAge(this.maxAge);
        activitySession.setCapacity(this.capacity);

        Activity activity = new Activity();
        activity.setId(this.activityId);
        activitySession.setActivity(activity);

        User user = new User();
        user.setId(this.hostId);
        activitySession.setHost(user);

        Location location = new Location();
        location.setId(this.locationId);
        activitySession.setLocation(location);
        return activitySession;
    }
}
