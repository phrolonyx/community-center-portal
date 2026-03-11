package com.manara.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
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

    @ManyToOne
    private ActivityAttendance attendance;

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActivitySession getActivitySession() {
        return activitySession;
    }

    public void setActivitySession(ActivitySession activitySession) {
        this.activitySession = activitySession;
    }

    public ParentProfile getParentProfile() {
        return parentProfile;
    }

    public void setParentProfile(ParentProfile parentProfile) {
        this.parentProfile = parentProfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(LocalDateTime confirmedAt) {
        this.confirmedAt = confirmedAt;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public List<ActivityRegistrationLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ActivityRegistrationLog> logs) {
        this.logs = logs;
    }

    public ActivityAttendance getAttendance() {
        return attendance;
    }

    public void setAttendance(ActivityAttendance attendance) {
        this.attendance = attendance;
    }

    public Boolean isConfirmed() {
        return confirmedAt != null;
    }

    public Boolean isCancelled() {
        return cancelledAt != null;
    }

}
