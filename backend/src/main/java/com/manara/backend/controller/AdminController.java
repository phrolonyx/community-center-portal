package com.manara.backend.controller;

import com.manara.backend.model.Activity;
import com.manara.backend.model.Child;
import com.manara.backend.model.User;
import com.manara.backend.repository.ActivityRepository;
import com.manara.backend.repository.ChildRepository;
import com.manara.backend.service.ChildService;
import com.manara.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChildService childService;

    @GetMapping("/admin/users")
    public String adminUsers(
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {

        List<User> users;

        // Si search est null, vide ou juste des espaces on affiche tout
        if (search == null || search.trim().isEmpty()) {
            users = userService.findAll();
            search = ""; // pour garder la barre vide dans le HTML
        } else {
            users = userService.search(search.trim());
        }

        model.addAttribute("users", users);
        model.addAttribute("search", search);

        return "page_admin_users";
    }

    @GetMapping("/admin/users/search-ajax")
    @ResponseBody
    public List<User> searchAjax(@RequestParam("search") String search) {

        if (search == null || search.trim().isEmpty()) {
            return userService.findAll();
        }

        return userService.search(search.trim());
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        // les stats des tableaux enfaite juste les nombres
        long totalUsers = userService.countAllUsers();
        long totalParents = userService.countUsersByRole("PARENT");
        long totalAdmins = userService.countUsersByRole("ADMIN");
        long totalDisabled = userService.countDisabledUsers();

        // nouvelles inscriptions a approuver
        List<User> pendingUsers = userService.findPendingUsers();

        // envoi au html
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalParents", totalParents);
        model.addAttribute("totalAdmins", totalAdmins);
        model.addAttribute("totalDisabled", totalDisabled);

        model.addAttribute("pendingUsers", pendingUsers);

        return "page_admin_dashboard";



    }

    @GetMapping("/admin/children")
    public String adminChildren(
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {

        List<Child> children;

        if (search == null || search.trim().isEmpty()) {
            children = childService.findAll();
            search = ""; // pour garder la barre vide
        } else {
            children = childService.searchByName(search.trim());
        }

        model.addAttribute("children", children);
        model.addAttribute("search", search);

        return "page_admin_enfants";
    }

    
















}


