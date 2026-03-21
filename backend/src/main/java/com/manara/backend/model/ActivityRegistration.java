package com.manara.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ActivityRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ParentProfile parentProfile;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ActivitySession activitySession;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Child child;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;

    private LocalDateTime cancelledAt;

    @OneToMany(mappedBy = "registration")
    private List<ActivityRegistrationLog> logs;

    public Boolean isConfirmed() {
        return confirmedAt != null;
    }

    public Boolean isCancelled() {
        return cancelledAt != null;
    }

}
