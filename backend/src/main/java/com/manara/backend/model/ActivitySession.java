package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ActivitySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "L'activité doit avoir un début")
    private LocalDateTime dateTimeStart;

    @Column(nullable = false)
    @NotBlank(message = "L'activité doit avoir un fin")
    private LocalDateTime dateTimeEnd;

    @Positive(message = "L'activité ne peut avoir une capacité négative")
    private Integer capacity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Activity activity;

    @ManyToOne
    private Location location;

    @ManyToOne
    @JoinColumn(name = "host_name_user_id")
    private User host;

    public boolean isFull() {
        return capacity.equals(location.getCapacity());
    }

    public Integer getAvailableSpots() {
        return location.getCapacity() - capacity;
    }
}
