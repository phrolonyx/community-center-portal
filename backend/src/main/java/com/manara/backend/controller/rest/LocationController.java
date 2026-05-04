package com.manara.backend.controller.rest;

import com.manara.backend.dto.LocationResponseDto;
import com.manara.backend.dto.UserResponseDto;
import com.manara.backend.model.User;
import com.manara.backend.service.LocationService;
import com.manara.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
@PreAuthorize("hasAuthority('ADMIN')")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponseDto>> getLocations(){
        return ResponseEntity.ok(
                locationService.findAll().stream().map(LocationResponseDto::from).toList()
        );
    }
}
