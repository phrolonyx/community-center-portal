package com.manara.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdminDashboardDto {
    private Integer totalUsers;
    private Integer totalParents;
    private Integer totalAnimateurs;
    private Integer totalDisabled;
    private List<UserResponseDto> pendingUsers;

    public static AdminDashboardDto from(Integer totalUsers, Integer totalParents,
                                         Integer totalAnimateurs, Integer totalDisabled,
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
