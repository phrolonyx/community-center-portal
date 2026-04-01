package com.manara.backend.controller;

import com.manara.backend.model.*;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.ActivityService;
import com.manara.backend.service.ActivitySessionService;
import com.manara.backend.service.ChildService;
import com.manara.backend.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ParentController {

    @Autowired
    private ParentService parentService;

    @Autowired
    private ChildService childService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivitySessionService activitySessionService;

    @GetMapping("/dashboardParent")
    public String dashboardParent(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.get(0);
            List<Child> children = childService.findByParentProfileId(profile.getId());
            model.addAttribute("parentProfile", profile);
            model.addAttribute("children", children);
        }

        return "dashboardParent";
    }

    @GetMapping("/dashboardParent/enfants")
    public String enfants(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.get(0);
            List<Child> children = childService.findByParentProfileId(profile.getId());
            model.addAttribute("children", children);
            model.addAttribute("parentProfileId", profile.getId());
        }

        return "enfants";
    }

    @PostMapping("/dashboardParent/enfants/ajouter")
    public String ajouterEnfant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String dateOfBirth,
            @RequestParam String relationship,
            @RequestParam(required = false) String medical
    ) {
        User user = userDetails.getUser();
        List<ParentProfile> profiles = parentService.findByUserId(user.getId());

        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.get(0);

            Child child = new Child();
            child.setFirstName(firstName);
            child.setLastName(lastName);
            child.setDateOfBirth(LocalDate.parse(dateOfBirth).atStartOfDay());
            child.setUpdatedAt(LocalDateTime.now());
            childService.createChild(child, profile.getId());

            if (medical != null && !medical.trim().isEmpty()) {
                ChildMedical condition = new ChildMedical();
                condition.setCondition(medical.trim());
                condition.setSeverity("Non spécifiée");
                condition.setUpdatedAt(LocalDateTime.now());
                childService.addMedicalCondition(condition, child.getId());
            }
        }

        return "redirect:/dashboardParent/enfants?ajout";
    }

    @PostMapping("/dashboardParent/enfants/modifier")
    public String modifierEnfant(
            @RequestParam Integer childId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String dateOfBirth,
            @RequestParam(required = false) String medical
    ) {
        Child child = childService.findById(childId);
        child.setFirstName(firstName);
        child.setLastName(lastName);
        child.setDateOfBirth(LocalDate.parse(dateOfBirth).atStartOfDay());
        child.setUpdatedAt(LocalDateTime.now());
        childService.updateChild(child);

        if (medical != null && !medical.trim().isEmpty()) {
            List<ChildMedical> conditions = child.getMedicalConditions();

            if (conditions != null && !conditions.isEmpty()) {
                ChildMedical existing = conditions.get(0);
                existing.setCondition(medical.trim());
                existing.setUpdatedAt(LocalDateTime.now());
                childService.updateMedicalCondition(existing);
            } else {
                ChildMedical condition = new ChildMedical();
                condition.setCondition(medical.trim());
                condition.setSeverity("Non spécifiée");
                condition.setUpdatedAt(LocalDateTime.now());
                childService.addMedicalCondition(condition, child.getId());
            }
        }

        return "redirect:/dashboardParent/enfants?modif";
    }

    @PostMapping("/dashboardParent/enfants/supprimer")
    public String supprimerEnfant(@RequestParam Integer childId) {
        childService.deleteChild(childId);
        return "redirect:/dashboardParent/enfants?suppr";
    }

    @GetMapping("/dashboardParent/activites")
    public String activites(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        model.addAttribute("seances", activitySessionService.findAll());

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.get(0);
            model.addAttribute("children", childService.findByParentProfileId(profile.getId()));
            model.addAttribute("registrations", activityService.findRegistrationsByParentId(profile.getId()));
        }

        return "activites";
    }

    @PostMapping("/dashboardParent/activites/inscrire")
    public String inscrireEnfant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Integer childId,
            @RequestParam Integer sessionId
    ) {
        List<ParentProfile> profiles = parentService.findByUserId(userDetails.getUser().getId());
        if (profiles.isEmpty()) {
            return "redirect:/dashboardParent/activites";
        }

        if (activityService.isAlreadyRegistered(childId, sessionId)) {
            return "redirect:/dashboardParent/activites?doublon";
        }

        ParentProfile profile = profiles.get(0);
        Child child = childService.findById(childId);
        ActivitySession seance = activitySessionService.findById(sessionId);

        if (!estDansTrancheAge(child.getAge(), seance.getMinAge(), seance.getMaxAge())) {
            return "redirect:/dashboardParent/activites?erreurAge";
        }

        ActivityRegistration registration = new ActivityRegistration();
        registration.setParentProfile(profile);
        registration.setChild(child);
        registration.setActivitySession(seance);
        registration.setStatus("En attente");
        registration.setCreatedAt(LocalDateTime.now());
        activityService.saveRegistration(registration);

        return "redirect:/dashboardParent/activites?inscrit";
    }

    private boolean estDansTrancheAge(Integer age, Integer min, Integer max) {
        if (min != null && age < min) return false;
        if (max != null && age > max) return false;
        return true;
    }

    @GetMapping("/dashboardParent/inscriptions")
    public String inscriptions(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.get(0);
            List<Child> children = childService.findByParentProfileId(profile.getId());
            List<ActivityRegistration> registrations = activityService.findRegistrationsByParentId(profile.getId());
            model.addAttribute("children", children);
            model.addAttribute("registrations", registrations);
        }

        return "inscriptionParent";
    }

    @PostMapping("/dashboardParent/inscriptions/annuler")
    public String annulerInscription(@RequestParam Integer registrationId) {
        activityService.cancelRegistration(registrationId);
        return "redirect:/dashboardParent/inscriptions?annule";
    }

    @GetMapping("/dashboardParent/messages")
    public String messages(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);
        return "messages";
    }

    @GetMapping("/dashboardParent/profil")
    public String profil(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        model.addAttribute("user", user);

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            model.addAttribute("parentProfile", profiles.get(0));
        }

        return "profil";
    }

    @PostMapping("/dashboardParent/profil/modifier")
    public String modifierProfil(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String addressLine1,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String emergencyName,
            @RequestParam(required = false) String emergencyRelationship,
            @RequestParam(required = false) String emergencyPhone
    ) {
        User user = userDetails.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        parentService.updateUser(user);

        List<ParentProfile> profiles = parentService.findByUserId(user.getId());
        if (!profiles.isEmpty()) {
            ParentProfile profile = profiles.get(0);
            profile.setPhoneNumber(phoneNumber);
            profile.setAddressLine1(addressLine1);
            profile.setCity(city);
            profile.setProvince(province);
            profile.setEmergencyName(emergencyName);
            profile.setEmergencyRelationship(emergencyRelationship);
            profile.setEmergencyPhone(emergencyPhone);
            parentService.updateProfile(profile);
        }

        return "redirect:/dashboardParent/profil?modif";
    }
}
