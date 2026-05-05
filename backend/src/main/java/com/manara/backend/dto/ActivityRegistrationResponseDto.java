package com.manara.backend.dto;

import com.manara.backend.model.ActivityRegistration;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityRegistrationResponseDto {

    private Integer id;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;

    private Integer childId;
    private String childFullName;

    private Integer activitySessionId;
    private String activityName;
    private LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;
    private String locationName;
    private String hostFullName;

    private Integer parentProfileId;

    public static ActivityRegistrationResponseDto from(ActivityRegistration activityRegistration) {
        ActivityRegistrationResponseDto dto = new ActivityRegistrationResponseDto();
        dto.setId(activityRegistration.getId());
        dto.setStatus(activityRegistration.getStatus());
        dto.setCreatedAt(activityRegistration.getCreatedAt());
        dto.setConfirmedAt(activityRegistration.getConfirmedAt());
        dto.setCancelledAt(activityRegistration.getCancelledAt());

        if (activityRegistration.getChild() != null) {
            dto.setChildId(activityRegistration.getChild().getId());
            dto.setChildFullName(activityRegistration.getChild().getFullName());
        }
        if (activityRegistration.getActivitySession() != null) {
            dto.setActivitySessionId(activityRegistration.getActivitySession().getId());
            dto.setSessionStart(activityRegistration.getActivitySession().getDateTimeStart());
            dto.setSessionEnd(activityRegistration.getActivitySession().getDateTimeEnd());
            if (activityRegistration.getActivitySession().getActivity() != null)
                dto.setActivityName(activityRegistration.getActivitySession().getActivity().getName());
            if (activityRegistration.getActivitySession().getLocation() != null)
                dto.setLocationName(activityRegistration.getActivitySession().getLocation().getName());
            if (activityRegistration.getActivitySession().getHost() != null)
                dto.setHostFullName(activityRegistration.getActivitySession().getHost().getFullName());
        }
        if (activityRegistration.getParentProfile() != null) {
            dto.setParentProfileId(activityRegistration.getParentProfile().getId());
        }
        return dto;
    }
}