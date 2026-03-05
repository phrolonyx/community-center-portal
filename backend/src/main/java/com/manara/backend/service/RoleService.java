package com.manara.backend.service;

import com.manara.backend.model.Role;
import com.manara.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow();
    }

    public Role getDefaultRole() {
        return roleRepository.findByIsDefaultTrue().orElseThrow();
    }

}
