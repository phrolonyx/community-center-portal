package com.manara.backend.controller.rest;

import com.manara.backend.dto.AdminDashboardDto;
import com.manara.backend.model.User;
import com.manara.backend.service.ActivitySessionService;
import com.manara.backend.service.ChildService;
import com.manara.backend.service.LocationService;
import com.manara.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private ActivitySessionService sessionService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChildService childService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // les stats des tableaux enfaite juste les nombres
        long totalUsers = userService.countAllUsers();
        long totalParents = userService.countUsersByRole("PARENT");
        long totalAnimateurs = userService.countUsersByRole("ANIMATEUR");
        long totalDisabled = userService.countDisabledUsers();

        // nouvelles inscriptions a approuver
        List<User> pendingUsers = userService.findPendingUsers();

        // envoi au html
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalParents", totalParents);
        model.addAttribute("totalAnimateurs", totalAnimateurs);
        model.addAttribute("totalDisabled", totalDisabled);

        model.addAttribute("pendingUsers", pendingUsers);

        return AdminDashboardDto.from(totalUsers, totalParents, totalAnimateurs, totalDisabled, pendingUsers);



    }
}
