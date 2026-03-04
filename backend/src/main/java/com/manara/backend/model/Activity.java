package com.manara.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String name;

    private String description;

    private String activityImagePath;

    @OneToMany(mappedBy = "activity")
    private List<ActivitySession> sessions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActivityImagePath() {
        return activityImagePath;
    }

    public void setActivityImagePath(String activityImagePath) {
        this.activityImagePath = activityImagePath;
    }

    public List<ActivitySession> getSessions() {
        return sessions;
    }

    public void setSessions(List<ActivitySession> sessions) {
        this.sessions = sessions;
    }
}
