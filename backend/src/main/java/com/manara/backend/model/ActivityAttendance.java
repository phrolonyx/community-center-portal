package com.manara.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Data
public class ActivityAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "marked_by_user_id")
    private User markedBy;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ActivityRegistration registration;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime markedAt;

    private String note;

    public Boolean isPresent() {
        return markedBy != null;
    }
}
