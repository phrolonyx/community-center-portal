package com.manara.backend.dto;

import com.manara.backend.model.ActivitySession;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivitySessionResponseDto {

    private Integer id;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Integer capacity;

    private Integer activityId;
    private String activityName;
    private Integer locationId;
    private String locationName;
    private Integer hostId;
    private String hostFullName;

    private Integer minAge;
    private Integer maxAge;

    // Nombre d'enfants CONFIRMÉS pour cette séance (calculé côté service)
    private Integer confirmedCount;

    public static ActivitySessionResponseDto from(ActivitySession activitySession) {
        return from(activitySession, null);
    }

    public static ActivitySessionResponseDto from(ActivitySession activitySession, Integer confirmedCount) {
        ActivitySessionResponseDto dto = new ActivitySessionResponseDto();
        dto.setId(activitySession.getId());
        dto.setDateTimeStart(activitySession.getDateTimeStart());
        dto.setDateTimeEnd(activitySession.getDateTimeEnd());
        dto.setCapacity(activitySession.getCapacity());
        dto.setMinAge(activitySession.getMinAge());
        dto.setMaxAge(activitySession.getMaxAge());
        dto.setConfirmedCount(confirmedCount != null ? confirmedCount : 0);

        if (activitySession.getActivity() != null) {
            dto.setActivityId(activitySession.getActivity().getId());
            dto.setActivityName(activitySession.getActivity().getName());
        }
        if (activitySession.getLocation() != null) {
            dto.setLocationId(activitySession.getLocation().getId());
            dto.setLocationName(activitySession.getLocation().getName());
        }
        if (activitySession.getHost() != null) {
            dto.setHostId(activitySession.getHost().getId());
            dto.setHostFullName(activitySession.getHost().getFullName());
        }
        return dto;
    }
}