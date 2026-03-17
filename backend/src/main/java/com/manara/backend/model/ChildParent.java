package com.manara.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public ParentProfile getParentProfile() {
        return parentProfile;
    }

    public void setParentProfile(ParentProfile parentProfile) {
        this.parentProfile = parentProfile;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public User getValidatedBy() {
        return validatedBy;
    }

    public void setValidatedBy(User validatedBy) {
        this.validatedBy = validatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean isValidated() {
        return validatedBy != null;
    }
}
