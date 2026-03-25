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

/*
 * Service pour gerer les activites et les inscriptions.
 */
@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

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

    @Autowired
    private ActivityRegistrationRepository activityRegistrationRepository;

    // Sauvegarder une nouvelle inscription
    @Transactional
    public void saveRegistration(ActivityRegistration registration) {
        activityRegistrationRepository.save(registration);
    }

    // Trouver toutes les inscriptions d'un parent
    public List<ActivityRegistration> findRegistrationsByParentId(Integer parentProfileId) {
        return activityRegistrationRepository.findByParentProfileId(parentProfileId);
    }

    // Annuler une inscription (change le statut a "Annule")
    @Transactional
    public void cancelRegistration(Integer registrationId) {
        ActivityRegistration reg = activityRegistrationRepository.findById(registrationId).orElseThrow();
        reg.setStatus("Annulé");
        reg.setCancelledAt(LocalDateTime.now());
        activityRegistrationRepository.save(reg);
    }
}
