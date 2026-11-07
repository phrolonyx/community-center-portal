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
 * Tourne CHAQUE MINUTE pour que les emails partent precis a l'heure :
 *  - rappel 24h avant la seance (fenetre 23h59 - 24h01)
 *  - rappel 2h avant la seance (fenetre 1h59 - 2h01)
 */
@Service
public class ReminderService {

    @Autowired
    private ActivityRegistrationRepository registrationRepository;

    @Autowired
    private EmailService emailService;

    private static final DateTimeFormatter FORMAT_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");

    // Tourne CHAQUE MINUTE (60_000 ms) — precision a la minute pres
    @Scheduled(fixedRate = 60_000)
    public void verifierRappels() {
        LocalDateTime maintenant = LocalDateTime.now();
        List<ActivityRegistration> inscriptions = registrationRepository.findAll();

        for (ActivityRegistration r : inscriptions) {
            if (r.getCancelledAt() != null) continue;

            ActivitySession seance = r.getActivitySession();
            if (seance == null || seance.getDateTimeStart() == null) continue;

            LocalDateTime debut = seance.getDateTimeStart();
            long minutesAvantDebut = Duration.between(maintenant, debut).toMinutes();

            // Rappel 24h : fenetre etroite 23h59 - 24h01 (1439 - 1441 min)
            if (!r.getReminder24Sent() && minutesAvantDebut >= 1439 && minutesAvantDebut <= 1441) {
                envoyerRappel24h(r);
                r.setReminder24Sent(true);
                registrationRepository.save(r);
            }

            // Rappel 2h : fenetre etroite 1h59 - 2h01 (119 - 121 min)
            if (!r.getReminder2Sent() && minutesAvantDebut >= 119 && minutesAvantDebut <= 121) {
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
                ? r.getActivitySession().getLocation().getName() : "Centre Communautaire";

        emailService.envoyerRappel24h(email, prenomParent, nomEnfant, nomActivite, dateSeance, lieu);
    }

    private void envoyerRappel2h(ActivityRegistration r) {
        String email = r.getParentProfile().getUser().getEmail();
        String prenomParent = r.getParentProfile().getUser().getFirstName();
        String nomEnfant = r.getChild().getFirstName() + " " + r.getChild().getLastName();
        String nomActivite = r.getActivitySession().getActivity().getName();
        String dateSeance = r.getActivitySession().getDateTimeStart().format(FORMAT_DATE);
        String lieu = r.getActivitySession().getLocation() != null
                ? r.getActivitySession().getLocation().getName() : "Centre Communautaire";

        emailService.envoyerRappel2h(email, prenomParent, nomEnfant, nomActivite, dateSeance, lieu);
    }
}
