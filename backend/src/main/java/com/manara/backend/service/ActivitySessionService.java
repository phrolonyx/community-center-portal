package com.manara.backend.service;

import com.manara.backend.dto.ActivitySessionRequest;
import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Location;
import com.manara.backend.model.User;
import com.manara.backend.repository.ActivitySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ActivitySessionService {
    @Autowired
    private ActivitySessionRepository sessionRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private UserService userService;

    public List<ActivitySession> findAll() {
        return sessionRepository.findAll();
    }

    public ActivitySession findById(Integer id) {
        return sessionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sceance d'activite non trouve"));
    }

    public ActivitySession save(ActivitySession session) {
        return sessionRepository.save(session);
    }

    public void deleteById(Integer id) {
        sessionRepository.deleteById(id);
    }

    public ActivitySession createSession(ActivitySessionRequest request) {
        // valider les ids passer en json si non lance avec findbyId response 404
        Activity activity = activityService.findById(request.getActivityId());
        User host = userService.findById(request.getHostId());
        Location location = locationService.findById(request.getLocationId());

        ActivitySession session = request.toActivitySession();

        session.setActivity(activity);
        session.setHost(host);
        session.setLocation(location);

        return sessionRepository.save(session);
    }

    public ActivitySession updateSession(Integer id, ActivitySessionRequest request) {

        ActivitySession activitySession = findById(id);

        activitySession.setDateTimeStart(request.getDateTimeStart());
        activitySession.setDateTimeEnd(request.getDateTimeEnd());
        activitySession.setCapacity(request.getCapacity());
        activitySession.setMinAge(request.getMinAge());
        activitySession.setMaxAge(request.getMaxAge());

        // valider les ids passer en json si non lance avec findbyId response 404
        Activity activity = activityService.findById(request.getActivityId());
        User host = userService.findById(request.getHostId());
        Location location = locationService.findById(request.getLocationId());

        activitySession.setActivity(activity);
        activitySession.setHost(host);
        activitySession.setLocation(location);

        return sessionRepository.save(activitySession);
    }

}
