package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Entity
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalDateTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public LocalDateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(LocalDateTime dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public boolean isFull() {
        return capacity.equals(location.getCapacity());
    }

    public Integer getAvailableSpots() {
        return location.getCapacity() - capacity;
    }
}
