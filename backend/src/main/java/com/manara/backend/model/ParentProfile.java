package com.manara.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ParentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(length = 20)
    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide")
    private String phoneNumber;

    @NotBlank(message = "Votre adresse est requise")
    private String addressLine1;

    private String addressLine2;

    @Column(length = 100)
    @NotBlank(message = "Ce champ est obligatoire")
    private String city;

    @Column(length = 100)
    @NotBlank(message = "Ce champ est obligatoire")
    private String province;

    @Column(length = 100)
    @NotBlank(message = "Ce champ est obligatoire")
    private String country;

    @Column(length = 20)
    @NotBlank(message = "Ce champ est obligatoire")
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

    @OneToMany(mappedBy = "parentProfile")
    private List<ChildParent> childLinks;

    public ParentProfile() {}

    public ParentProfile(User user) {
        this.user = user;
        updatedAt = LocalDateTime.now();
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
