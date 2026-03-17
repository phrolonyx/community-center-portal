package com.manara.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParentController {

    @GetMapping("/dashboardParent")
    public String dashboardParent() {
        return "dashboardParent";
    }

    @GetMapping("/dashboardParent/activites")
    public String activites() {
        return "activites";
    }

    @GetMapping("/dashboardParent/enfants")
    public String enfants() {
        return "enfants";
    }

    @GetMapping("/dashboardParent/inscriptions")
    public String inscriptions() {
        return "inscriptionParent";
    }

    @GetMapping("/dashboardParent/messages")
    public String messages() {
        return "messages";
    }

    @GetMapping("/dashboardParent/profil")
    public String profil() {
        return "profil";
    }


}
