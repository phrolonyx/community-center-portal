package com.manara.backend.service;

import com.manara.backend.model.ActivitySession;
import com.manara.backend.repository.ActivitySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivitySessionService {
    @Autowired
    private ActivitySessionRepository sessionRepository;

    public List<ActivitySession> findAll() {
        return sessionRepository.findAll();
    }

    public ActivitySession findById(Integer id) {
        return sessionRepository.findById(id).orElseThrow();
    }

    public ActivitySession save(ActivitySession session) {
        return sessionRepository.save(session);
    }

    public void deleteById(Integer id) {
        sessionRepository.deleteById(id);
    }
}
