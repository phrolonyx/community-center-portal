package com.manara.backend.service;

import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.repository.ActivityRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
 * Service automatique de rappels par email.
 * Tourne toutes les 15 minutes et envoie :
 *  - un rappel 24h avant chaque seance (envoye une seule fois grace au flag reminder24Sent)
 *  - un rappel 2h avant chaque seance (flag reminder2Sent)
 */
@Service
public class ReminderService {

    @Autowired
    private ActivityRegistrationRepository registrationRepository;

    @Autowired
    private EmailService emailService;

    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");

    // Tourne toutes les 15 minutes (900_000 ms)
    @Scheduled(fixedRate = 900_000)
    public void verifierRappels() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<ActivityRegistration> inscriptions = registrationRepository.findAll();

        for (ActivityRegistration r : inscriptions) {
            // On ne relance pas pour les inscriptions annulees
            if (r.getCancelledAt() != null) continue;

            ActivitySession seance = r.getActivitySession();
            if (seance == null || seance.getDateTimeStart() == null) continue;

            LocalDateTime debut = seance.getDateTimeStart();
            long minutesAvantDebut = Duration.between(maintenant, debut).toMinutes();

            // Rappel 24h : entre 23h45 et 24h15 avant la seance
            if (!r.getReminder24Sent() && minutesAvantDebut >= 1425 && minutesAvantDebut <= 1455) {
                envoyerRappel24h(r);
                r.setReminder24Sent(true);
                registrationRepository.save(r);
            }

            // Rappel 2h : entre 1h45 et 2h15 avant la seance
            if (!r.getReminder2Sent() && minutesAvantDebut >= 105 && minutesAvantDebut <= 135) {
                envoyerRappel2h(r);
                r.setReminder2Sent(true);
                registrationRepository.save(r);
            }
        }
    }

    private void envoyerRappel24h(ActivityRegistration r) {
        String email = r.getParentProfile().getUser().getEmail();
        String prenomParent = r.getParentProfile().getUser().getFirstName();
        String nomEnfant = r.getChild().getFirstName() + " " + r.getChild().getLastName();
        String nomActivite = r.getActivitySession().getActivity().getName();
        String dateSeance = r.getActivitySession().getDateTimeStart().format(FORMAT_DATE);
        String lieu = r.getActivitySession().getLocation() != null
                ? r.getActivitySession().getLocation().getName() : "Centre Manara";

        emailService.envoyerRappel24h(email, prenomParent, nomEnfant, nomActivite, dateSeance, lieu);
    }

    private void envoyerRappel2h(ActivityRegistration r) {
        String email = r.getParentProfile().getUser().getEmail();
        String prenomParent = r.getParentProfile().getUser().getFirstName();
        String nomEnfant = r.getChild().getFirstName() + " " + r.getChild().getLastName();
        String nomActivite = r.getActivitySession().getActivity().getName();
        String dateSeance = r.getActivitySession().getDateTimeStart().format(FORMAT_DATE);
        String lieu = r.getActivitySession().getLocation() != null
                ? r.getActivitySession().getLocation().getName() : "Centre Manara";

        emailService.envoyerRappel2h(email, prenomParent, nomEnfant, nomActivite, dateSeance, lieu);
    }
}
