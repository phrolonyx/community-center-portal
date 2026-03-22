package com.manara.backend.service;

import com.manara.backend.model.Activity;
import com.manara.backend.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
