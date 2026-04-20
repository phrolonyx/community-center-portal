package com.manara.backend.dto;

import com.manara.backend.model.ParentProfile;
import lombok.Data;

@Data
public class ParentProfileResponseDto {
    private Integer id;
    private String phoneNumber;
    private String addressLine1;
    private String city;
    private String province;
    private String postalCode;
    private String emergencyName;
    private String emergencyPhone;
    private String emergencyRelationship;

    public static ParentProfileResponseDto from(ParentProfile parentProfile) {
        ParentProfileResponseDto dto = new ParentProfileResponseDto();
        dto.setId(parentProfile.getId());
        dto.setPhoneNumber(parentProfile.getPhoneNumber());
        dto.setAddressLine1(parentProfile.getAddressLine1());
        dto.setCity(parentProfile.getCity());
        dto.setProvince(parentProfile.getProvince());
        dto.setPostalCode(parentProfile.getPostalCode());
        dto.setEmergencyName(parentProfile.getEmergencyName());
        dto.setEmergencyPhone(parentProfile.getEmergencyPhone());
        dto.setEmergencyRelationship(parentProfile.getEmergencyRelationship());
        return dto;
    }
}
