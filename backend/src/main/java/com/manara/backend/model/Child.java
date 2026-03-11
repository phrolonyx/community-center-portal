package com.manara.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDateTime dateOfBirth;

    private String childImagePath;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "child")
    private List<ChildMedical> medicalConditions;

    @OneToMany(mappedBy = "child")
    private List<ChildParent> parentLinks;

    @OneToMany(mappedBy = "child")
    private List<ActivityRegistration> registrations;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getChildImagePath() {
        return childImagePath;
    }

    public void setChildImagePath(String childImagePath) {
        this.childImagePath = childImagePath;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ChildMedical> getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(List<ChildMedical> medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public List<ChildParent> getParentLinks() {
        return parentLinks;
    }

    public void setParentLinks(List<ChildParent> parentLinks) {
        this.parentLinks = parentLinks;
    }

    public List<ActivityRegistration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<ActivityRegistration> registrations) {
        this.registrations = registrations;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Integer getAge() {
        return LocalDateTime.now().getYear() - dateOfBirth.getYear();
    }
}
