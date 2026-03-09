package com.manara.backend.service;

import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.User;
import com.manara.backend.repository.ParentProfileRepository;
import com.manara.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentService {

    @Autowired
    private ParentProfileRepository parentProfileRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createProfile(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        ParentProfile parentProfile = new ParentProfile(user);
        parentProfileRepository.save(parentProfile);
    }

    public List<ParentProfile> findByUserId(Integer userId) {
        return parentProfileRepository.findAllByUserId(userId);
    }

    @Transactional
    public void updateProfile(ParentProfile profile) {
        parentProfileRepository.save(profile);
    }
}
