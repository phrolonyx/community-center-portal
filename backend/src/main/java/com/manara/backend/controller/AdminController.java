package com.manara.backend.controller;

import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Child;
import com.manara.backend.model.User;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.repository.ActivityRepository;
import com.manara.backend.repository.ChildRepository;
import com.manara.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/MVC")
public class AdminController {

    @Autowired
    private ActivitySessionService sessionService;

    @Autowired
    private LocationService locationService;

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
    @PostMapping("/admin/users/disable/{id}")
    public String disableUser(@PathVariable Integer id) {

        User user = userService.findById(id);

        // Empêcher désactivation d’un admin
        if (user.getRole().getName().equalsIgnoreCase("ADMIN")) {
            return "redirect:/admin/users?error=admin-disable-forbidden";
        }

        user.setEnabled(false);
        userService.save(user);

        return "redirect:/admin/users?success=disabled";
    }

    @PostMapping("/admin/users/enable/{id}")
    public String enableUser(@PathVariable Integer id) {

        User user = userService.findById(id);
        user.setEnabled(true);
        userService.save(user);

        return "redirect:/admin/users?success=enabled";
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

        return "page_admin_dashboard";



    }

    @PostMapping("/admin/users/approve/{id}")
    public String approveUser(@PathVariable Integer id) {

        User user = userService.findById(id);

        user.setPending(false);
        user.setEnabled(true);

        userService.save(user);

        return "redirect:/admin/dashboard?approved";
    }

    @PostMapping("/admin/users/reject/{id}")
    public String rejectUser(@PathVariable Integer id) {

        User user = userService.findById(id);

        user.setPending(false);
        user.setEnabled(false);

        userService.save(user);

        return "redirect:/admin/dashboard?rejected";
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

    @PostMapping("/admin/children/delete/{id}")
    public String deleteChild(@PathVariable Integer id) {

        childService.deleteChild(id);

        return "redirect:/admin/children?success=deleted";
    }




    @Autowired
    private ActivityService activityService;

    @Autowired
    private ImageStorageService imageStorageService;

    // -------------------------
// GESTION DES ACTIVITÉS
// -------------------------
    @GetMapping("/admin/activities")
    public String adminActivities(Model model) {

        List<Activity> activities = activityService.findAll();
        model.addAttribute("activities", activities);

        return "page_admin_activites";
    }

    @GetMapping("/admin/activities/add")
    public String addActivityForm(Model model) {
        model.addAttribute("activity", new Activity());
        model.addAttribute("formTitle", "Ajouter une activité");
        return "page_admin_activite_form";
    }

    @GetMapping("/admin/activities/edit")
    public String editActivityForm(@RequestParam("id") Integer id, Model model) {
        Activity activity = activityService.findById(id);
        model.addAttribute("activity", activity);
        model.addAttribute("formTitle", "Modifier une activité");
        return "page_admin_activite_form";
    }

    @PostMapping("/admin/activities/save")
    public String saveActivity(@ModelAttribute Activity activity,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = imageStorageService.store(imageFile, "uploads/activities");
                activity.setActivityImagePath("/uploads/activities/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (activity.getId() != null) {
            Activity existing = activityService.findById(activity.getId());
            activity.setActivityImagePath(existing.getActivityImagePath());
        }

        activityService.save(activity);
        return "redirect:/admin/activities";
    }


    @PostMapping("/admin/activities/delete")
    public String deleteActivity(@RequestParam("id") Integer id) {
        activityService.deleteById(id);
        return "redirect:/admin/activities";
    }

    // -------------------------
    // GESTION DES SÉANCES
    // -------------------------
    @GetMapping("/admin/sessions")
    public String adminSessions(Model model) {
        model.addAttribute("sessions", sessionService.findAll());
        return "page_admin_sceances";
    }

    @GetMapping("/admin/sessions/add")
    public String addSessionForm(Model model) {
        model.addAttribute("session", new ActivitySession());
        model.addAttribute("activities", activityService.findAll());
        model.addAttribute("hosts", userService.findByRoleName("ANIMATEUR"));
        model.addAttribute("locations", locationService.findAll());
        return "page_admin_sceance_form";
    }

    @GetMapping("/admin/sessions/edit")
    public String editSessionForm(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("session", sessionService.findById(id));
        model.addAttribute("activities", activityService.findAll());
        model.addAttribute("hosts", userService.findByRoleName("ANIMATEUR"));
        model.addAttribute("locations", locationService.findAll());
        return "page_admin_sceance_form";
    }

    @PostMapping("/admin/sessions/save")
    public String saveSession(@ModelAttribute ActivitySession session) {
        sessionService.save(session);
        return "redirect:/admin/sessions";
    }

    @PostMapping("/admin/sessions/delete")
    public String deleteSession(@RequestParam("id") Integer id) {
        sessionService.deleteById(id);
        return "redirect:/admin/sessions";
    }

    @Autowired
    private MessageService messageService;

    // page admin pour voir et envoyer des messages aux parents
    @GetMapping("/admin/messages")
    public String adminMessages(Model model) {
        model.addAttribute("messages", messageService.findAll());
        model.addAttribute("parents", userService.findByRoleName("PARENT"));
        return "page_admin_messages";
    }

    // envoi depuis la page users (le modal)
    @PostMapping("/admin/messages/send-from-users")
    public String sendMessageFromUsers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String subject, @RequestParam String content,
            @RequestParam String type, @RequestParam(required = false) Integer recipientId
    ) {
        envoyerMessage(userDetails, subject, content, type, recipientId);
        return "redirect:/admin/users?msgEnvoye";
    }

    // envoi depuis la page messages
    @PostMapping("/admin/messages/send")
    public String sendMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String subject, @RequestParam String content,
            @RequestParam String type, @RequestParam(required = false) Integer recipientId
    ) {
        envoyerMessage(userDetails, subject, content, type, recipientId);
        return "redirect:/admin/messages?envoye";
    }

    // methode privee pour eviter de dupliquer le code d'envoi
    private void envoyerMessage(CustomUserDetails userDetails, String subject,
            String content, String type, Integer recipientId) {
        User sender = userDetails.getUser();
        User recipient = (recipientId != null) ? userService.findById(recipientId) : null;
        if (recipient != null) {
            messageService.sendMessage(sender, recipient, subject, content, type);
        } else {
            messageService.sendToAll(sender, subject, content, type);
        }
    }
}


