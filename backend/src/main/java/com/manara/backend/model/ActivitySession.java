package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ActivitySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "L'activité doit avoir un début")
    private LocalDateTime dateTimeStart;

    @Column(nullable = false)
    @NotNull(message = "L'activité doit avoir un fin")
    private LocalDateTime dateTimeEnd;

    @Positive(message = "L'activité ne peut avoir une capacité négative")
    private Integer capacity;

    @Column(nullable = false)
    private Integer minAge;

    @Column(nullable = false)
    private Integer maxAge;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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
