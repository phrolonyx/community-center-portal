package com.manara.backend.controller.mvc;

import com.manara.backend.dto.RegisterRequest;
import com.manara.backend.model.User;
import com.manara.backend.service.ParentService;
import com.manara.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*
 * Controller pour les pages Connexion et Inscription.
 * Gere la creation de compte et la verification AJAX du email.
 */
@Controller
public class ConnexionInscriptionController {

    @Autowired
    private UserService userService;

    @Autowired
    private ParentService parentService;

    // Affiche la page de connexion
    @GetMapping("/connexion")
    public String connexion() {
        return "connexion";
    }

    // Affiche le formulaire d'inscription
    @GetMapping("/inscription")
    public String inscription() {
        return "inscription";
    }

    // Page affichee si l'utilisateur n'a pas les droits
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_denied";
    }

    // Traite le formulaire d'inscription
    @PostMapping("/inscription")
    public String processInscription(
            @Valid @ModelAttribute RegisterRequest dto,
            BindingResult bindingResult,
            Model model
    ) {
        // Verifier les erreurs de validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Veuillez corriger les erreurs dans le formulaire.");
            return "inscription";
        }

        // Verifier que les mots de passe correspondent
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas.");
            return "inscription";
        }

        // Verifier que le email n'est pas deja utilise
        if (userService.emailExists(dto.getEmail())) {
            model.addAttribute("error", "Cette adresse courriel est déjà utilisée.");
            return "inscription";
        }

        // Creer le compte utilisateur
        User user = userService.registerUser(dto);

        // Si c'est un parent, creer aussi son profil parent
        if ("PARENT".equals(dto.getRole())) {
            parentService.createProfileFromInscription(user, dto);
        }

        // Rediriger vers connexion avec message de succes
        return "redirect:/connexion?inscrit";
    }

    // Verification AJAX : est-ce que le email existe deja ?
    @GetMapping("/inscription/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.emailExists(email);
        return Map.of("exists", exists);
    }
}
