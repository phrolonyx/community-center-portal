package com.manara.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminDashboardDto {
    private Long totalUsers;
    private Long totalParents;
    private Long totalAnimateurs;
    private Long totalDisabled;
    private List<UserResponseDto> pendingUsers;

    public static AdminDashboardDto from(Long totalUsers, Long totalParents,
                                         Long totalAnimateurs, Long totalDisabled,
                                         List<UserResponseDto> pendingUsers) {
        AdminDashboardDto dto = new AdminDashboardDto();
        dto.setTotalUsers(totalUsers);
        dto.setTotalParents(totalParents);
        dto.setTotalAnimateurs(totalAnimateurs);
        dto.setTotalDisabled(totalDisabled);
        dto.setPendingUsers(pendingUsers);
        return dto;
    }
}
