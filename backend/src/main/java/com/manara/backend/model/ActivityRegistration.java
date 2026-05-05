package com.manara.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    // Flags pour ne pas envoyer le meme rappel email plusieurs fois
    @Column(nullable = false)
    private Boolean reminder24Sent = false;

    @Column(nullable = false)
    private Boolean reminder2Sent = false;

    @OneToMany(
            mappedBy = "registration",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ActivityRegistrationLog> logs;

    public Boolean isConfirmed() {
        return confirmedAt != null;
    }

    public Boolean isCancelled() {
        return cancelledAt != null;
    }

}
