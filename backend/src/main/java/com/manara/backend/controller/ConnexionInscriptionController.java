package com.manara.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConnexionInscriptionController {

    @GetMapping("/connexion")
    public String connexion() {
        return "connexion";
    }

    @GetMapping("/inscription")
    public String inscription() {
        return "inscription";
    }

}
