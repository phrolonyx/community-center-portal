package com.manara.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
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
}
