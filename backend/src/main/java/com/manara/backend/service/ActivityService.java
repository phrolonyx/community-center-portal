package com.manara.backend.service;

import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.repository.ActivityRegistrationRepository;
import com.manara.backend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityRegistrationRepository registrationRepository;

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Activity findById(Integer id) {
        return activityRepository.findById(id).orElseThrow();
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void deleteById(Integer id) {
        activityRepository.deleteById(id);
    }

    public boolean isAlreadyRegistered(Integer childId, Integer sessionId) {
        return registrationRepository.existsByChildIdAndActivitySessionIdAndStatusNot(
                childId, sessionId, "Annulé"
        );
    }

    @Transactional
    public void saveRegistration(ActivityRegistration registration) {
        registrationRepository.save(registration);
    }

    public ActivityRegistration findRegistrationById(Integer id) {
        return registrationRepository.findById(id).orElseThrow();
    }

    public List<ActivityRegistration> findRegistrationsByParentId(Integer parentProfileId) {
        return registrationRepository.findByParentProfileId(parentProfileId);
    }

    @Transactional
    public void cancelRegistration(Integer registrationId) {
        ActivityRegistration registration = registrationRepository.findById(registrationId).orElseThrow();
        registration.setStatus("Annulé");
        registration.setCancelledAt(LocalDateTime.now());
        registrationRepository.save(registration);
    }
}
