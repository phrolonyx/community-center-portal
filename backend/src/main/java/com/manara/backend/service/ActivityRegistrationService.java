package com.manara.backend.service;

import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.repository.ActivityRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ActivityRegistrationService {

    @Autowired
    private ActivityRegistrationRepository activityRegistrationRepository;

    public List<ActivityRegistration> findByParentProfileId(Integer parentProfileId) {
        return activityRegistrationRepository.findByParentProfileId(parentProfileId);
    }

    public ActivityRegistration findById(Integer id) {
        return activityRegistrationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscription non trouvee"));
    }

    public ActivityRegistration save(ActivityRegistration registration) {
        return activityRegistrationRepository.save(registration);
    }

    public boolean existsActiveRegistration(Integer childId, Integer sessionId) {
        return activityRegistrationRepository
                .existsByChildIdAndActivitySessionIdAndStatusNot(childId, sessionId, "CANCELLED");
    }
}
