package com.manara.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"child_id", "parent_profile_id"}))
public class ChildParent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Child child;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ParentProfile parentProfile;

    @Column(length = 100)
    private String relationship;

    @Column(nullable = false)
    private Boolean isPrimary;

    @ManyToOne
    @JoinColumn(name = "validated_by_user_id")
    private User validatedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ChildParent() {}

    public ChildParent(Child child, ParentProfile parentProfile) {
        this.child = child;
        this.parentProfile = parentProfile;
        isPrimary = true;
        validatedBy = null;
        createdAt = LocalDateTime.now();
    }

    public Boolean isValidated() {
        return validatedBy != null;
    }
}
