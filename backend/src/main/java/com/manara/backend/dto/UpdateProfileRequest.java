package com.manara.backend.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String addressLine1;
    private String city;
    private String province;
    private String emergencyName;
    private String emergencyRelationship;
    private String emergencyPhone;
}
