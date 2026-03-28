package com.manara.backend.controller;

import com.manara.backend.model.Activity;
import com.manara.backend.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/")
    public String index() {
        return "accueil";
    }

    @GetMapping("/activities")
    public String adminActivities(Model model) {

        List<Activity> activities = activityService.findAll();
        model.addAttribute("activities", activities);

        return "view_activities";
    }
}
