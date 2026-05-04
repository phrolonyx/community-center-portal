package com.manara.backend.dto;

import com.manara.backend.model.Location;
import com.manara.backend.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LocationResponseDto {
    private Integer id;
    private String address;
    private String description;
    private Integer capacity;
    private String name;


    public static LocationResponseDto from(Location location){
        LocationResponseDto dto = new LocationResponseDto();
        dto.setId(location.getId());
        dto.setAddress(location.getAddress());
        dto.setDescription(location.getDescription());
        dto.setCapacity(location.getCapacity());
        dto.setName(location.getName());

        return dto;
    }
}
