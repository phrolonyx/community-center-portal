package com.manara.backend.service;

import com.manara.backend.model.Location;
import com.manara.backend.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public Location findById(Integer id) {
        return locationRepository.findById(id).orElseThrow();
    }
}