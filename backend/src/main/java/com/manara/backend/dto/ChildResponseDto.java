package com.manara.backend.dto;

import com.manara.backend.model.Child;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChildResponseDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private Integer age;
    private List<MedicalInfo> medicalConditions;

    @Data
    public static class MedicalInfo {
        private Integer id;
        private String condition;
        private String severity;
        private String notes;
    }

    public static ChildResponseDto from(Child child) {
        ChildResponseDto dto = new ChildResponseDto();
        dto.setId(child.getId());
        dto.setFirstName(child.getFirstName());
        dto.setLastName(child.getLastName());
        dto.setDateOfBirth(child.getDateOfBirth());
        dto.setAge(child.getAge());

        if (child.getMedicalConditions() != null) {
            dto.setMedicalConditions(
                    child.getMedicalConditions().stream().map(medicalCondition -> {
                        MedicalInfo medicalInfo = new MedicalInfo();
                        medicalInfo.setId(medicalCondition.getId());
                        medicalInfo.setCondition(medicalCondition.getCondition());
                        medicalInfo.setSeverity(medicalCondition.getSeverity());
                        medicalInfo.setNotes(medicalCondition.getNotes());
                        return medicalInfo;
                    }).toList()
            );
        }
        return dto;
    }
}
