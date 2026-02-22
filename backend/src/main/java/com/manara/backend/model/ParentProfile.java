package com.manara.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ParentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(length = 20)
    private String phoneNumber;

    private String addressLine1;

    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String province;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 200)
    private String emergencyName;

    @Column(length = 20)
    private String emergencyPhone;

    @Column(length = 100)
    private String emergencyRelationship;

    @ManyToOne
    private TermsVersion termsVersion;

    private LocalDateTime termsAcceptAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public ParentProfile() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    public String getEmergencyRelationship() {
        return emergencyRelationship;
    }

    public void setEmergencyRelationship(String emergencyRelationship) {
        this.emergencyRelationship = emergencyRelationship;
    }

    public TermsVersion getTermsVersion() {
        return termsVersion;
    }

    public void setTermsVersion(TermsVersion termsVersion) {
        this.termsVersion = termsVersion;
    }

    public LocalDateTime getTermsAcceptAt() {
        return termsAcceptAt;
    }

    public void setTermsAcceptAt(LocalDateTime termsAcceptAt) {
        this.termsAcceptAt = termsAcceptAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFullAddress1() {
        return addressLine1 + " " + city + " " + province + " " + postalCode;
    }

    public String getFullAddress2() {
        return addressLine2 + " " + city + " " + province + " " + postalCode;
    }

    public Boolean hasAcceptedTerms() {
        return termsAcceptAt != null;
    }
}
