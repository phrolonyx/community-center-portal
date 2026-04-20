package com.manara.backend.dto;

import com.manara.backend.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String roleName;
    private Boolean enabled;
    private Boolean pending;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public static UserResponseDto from(User user){
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEnabled(user.getEnabled());
        dto.setPending(user.getPending());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLogin(user.getLastLogin());
        dto.setRoleName(user.getRole().getName());
        return dto;
    }
}
