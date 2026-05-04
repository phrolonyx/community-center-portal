package com.manara.backend.dto;

import com.manara.backend.model.Activity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActivityResponseDto {

    private Integer id;
    private String name;
    private String description;
    private String activityImagePath;
    private List<SessionInfo> sessions;

    @Data
    public static class SessionInfo {
        private Integer id;
        private LocalDateTime dateTimeStart;
        private LocalDateTime dateTimeEnd;
        private Integer capacity;
        private String locationName;
        private String hostFullName;
        private Integer minAge;
        private Integer maxAge;
    }

    public static ActivityResponseDto from(Activity activity) {
        ActivityResponseDto dto = new ActivityResponseDto();
        dto.setId(activity.getId());
        dto.setName(activity.getName());
        dto.setDescription(activity.getDescription());
        dto.setActivityImagePath(activity.getActivityImagePath());

        if (activity.getSessions() != null) {
            dto.setSessions(
                    activity.getSessions().stream().map(session -> {
                        SessionInfo sessionInfo = new SessionInfo();
                        sessionInfo.setId(session.getId());
                        sessionInfo.setDateTimeStart(session.getDateTimeStart());
                        sessionInfo.setDateTimeEnd(session.getDateTimeEnd());
                        sessionInfo.setCapacity(session.getCapacity());
                        if (session.getLocation() != null) sessionInfo.setLocationName(session.getLocation().getName());
                        if (session.getHost() != null)     sessionInfo.setHostFullName(session.getHost().getFullName());
                        sessionInfo.setMinAge(session.getMinAge());
                        sessionInfo.setMaxAge(session.getMaxAge());
                        return sessionInfo;
                    }).toList()
            );
        }
        return dto;
    }
}