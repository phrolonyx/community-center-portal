package com.manara.backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/*
 * Service pour l'envoi de courriels via Gmail SMTP.
 * Utilise un template HTML avec les couleurs du Centre Manara.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // adresse courriel configuree dans application.properties
    @Value("${app.mail.from}")
    private String fromEmail;

    // envoie un courriel en format HTML
    public void envoyerEmail(String destinataire, String sujet, String contenuHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destinataire);
            helper.setSubject(sujet);
            helper.setText(contenuHtml, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Erreur envoi email a " + destinataire + " : " + e.getMessage());
        }
    }

    // courriel de bienvenue quand un nouveau user s'inscrit
    public void envoyerBienvenue(String email, String prenom) {
        String html = construireTemplate(
            "Bienvenue, " + prenom + " !",
            "<p>Merci de vous etre inscrit au <strong>Centre Manara</strong>.</p>"
            + "<p>Votre compte a ete cree avec succes. Vous pouvez maintenant :</p>"
            + "<ul>"
            + "  <li>Ajouter vos enfants a votre profil</li>"
            + "  <li>Les inscrire aux activites disponibles</li>"
            + "  <li>Suivre leurs inscriptions en temps reel</li>"
            + "</ul>"
            + "<p>Connectez-vous des maintenant pour commencer !</p>"
        );
        envoyerEmail(email, "Bienvenue au Centre Manara !", html);
    }

    // courriel envoye au parent quand il inscrit son enfant a une activite
    public void envoyerConfirmationInscription(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance) {
        String html = construireTemplate(
            "Inscription enregistree !",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>L'inscription de <strong>" + nomEnfant + "</strong> a l'activite "
            + "<strong>" + nomActivite + "</strong> a bien ete enregistree.</p>"
            + "<table style=\"width:100%; border-collapse:collapse; margin:16px 0;\">"
            + "<tr><td style=\"padding:10px 14px; background:#F0F4F8; font-weight:700; color:#043C5C;\">Enfant</td>"
            + "<td style=\"padding:10px 14px; background:#F0F4F8;\">" + nomEnfant + "</td></tr>"
            + "<tr><td style=\"padding:10px 14px; font-weight:700; color:#043C5C;\">Activite</td>"
            + "<td style=\"padding:10px 14px;\">" + nomActivite + "</td></tr>"
            + "<tr><td style=\"padding:10px 14px; background:#F0F4F8; font-weight:700; color:#043C5C;\">Date</td>"
            + "<td style=\"padding:10px 14px; background:#F0F4F8;\">" + dateSeance + "</td></tr>"
            + "<tr><td style=\"padding:10px 14px; font-weight:700; color:#043C5C;\">Statut</td>"
            + "<td style=\"padding:10px 14px;\">En attente</td></tr>"
            + "</table>"
            + "<p>Vous recevrez une notification lorsque l'inscription sera confirmee.</p>"
        );
        envoyerEmail(email, "Inscription confirmee - " + nomActivite, html);
    }

    // courriel quand une inscription est annulee
    public void envoyerAnnulation(String email, String prenomParent,
            String nomEnfant, String nomActivite) {
        String html = construireTemplate(
            "Inscription annulee",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>L'inscription de <strong>" + nomEnfant + "</strong> a l'activite "
            + "<strong>" + nomActivite + "</strong> a ete annulee.</p>"
            + "<p>Si cette annulation n'est pas de votre initiative, contactez l'administration.</p>"
        );
        envoyerEmail(email, "Annulation - " + nomActivite, html);
    }

    // courriel quand l'admin CONFIRME une inscription en attente
    public void envoyerConfirmationAdmin(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance) {
        String html = construireTemplate(
            "Inscription confirmee !",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>Bonne nouvelle ! L'inscription de <strong>" + nomEnfant + "</strong> "
            + "a l'activite <strong>" + nomActivite + "</strong> a ete <strong>confirmee</strong> par l'administration.</p>"
            + "<p><strong>Date :</strong> " + dateSeance + "</p>"
            + "<p>A bientot au Centre Manara !</p>"
        );
        envoyerEmail(email, "Inscription confirmee - " + nomActivite, html);
    }

    // courriel quand l'admin ANNULE toute une seance (tous les inscrits sont notifies)
    public void envoyerAnnulationSeance(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance) {
        String html = construireTemplate(
            "Seance annulee",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>Nous sommes desoles de vous informer que la seance "
            + "<strong>" + nomActivite + "</strong> du <strong>" + dateSeance + "</strong> "
            + "a laquelle <strong>" + nomEnfant + "</strong> etait inscrit a ete annulee.</p>"
            + "<p>Pour plus d'informations, contactez l'administration du Centre Manara.</p>"
        );
        envoyerEmail(email, "Seance annulee - " + nomActivite, html);
    }

    // rappel 24h avant la seance
    public void envoyerRappel24h(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance, String lieu) {
        String html = construireTemplate(
            "Rappel : activite demain",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>Petit rappel : <strong>" + nomEnfant + "</strong> participe demain a "
            + "<strong>" + nomActivite + "</strong>.</p>"
            + "<table style=\"width:100%; border-collapse:collapse; margin:16px 0;\">"
            + "<tr><td style=\"padding:10px 14px; background:#F0F4F8; font-weight:700; color:#043C5C;\">Activite</td>"
            + "<td style=\"padding:10px 14px; background:#F0F4F8;\">" + nomActivite + "</td></tr>"
            + "<tr><td style=\"padding:10px 14px; font-weight:700; color:#043C5C;\">Date</td>"
            + "<td style=\"padding:10px 14px;\">" + dateSeance + "</td></tr>"
            + "<tr><td style=\"padding:10px 14px; background:#F0F4F8; font-weight:700; color:#043C5C;\">Lieu</td>"
            + "<td style=\"padding:10px 14px; background:#F0F4F8;\">" + lieu + "</td></tr>"
            + "</table>"
            + "<p>A demain au Centre Manara !</p>"
        );
        envoyerEmail(email, "Rappel demain - " + nomActivite, html);
    }

    // rappel 2h avant la seance
    public void envoyerRappel2h(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance, String lieu) {
        String html = construireTemplate(
            "Rappel : activite dans 2 heures !",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>L'activite <strong>" + nomActivite + "</strong> commence dans <strong>2 heures</strong>.</p>"
            + "<p><strong>" + nomEnfant + "</strong> est attendu a <strong>" + dateSeance + "</strong> "
            + "au lieu suivant : <strong>" + lieu + "</strong>.</p>"
            + "<p>A tout de suite au Centre Manara !</p>"
        );
        envoyerEmail(email, "Activite dans 2h - " + nomActivite, html);
    }

    // genere le HTML du courriel avec le header bleu + barre jaune Manara
    private String construireTemplate(String titre, String contenu) {
        return "<!DOCTYPE html>"
            + "<html><head><meta charset=\"UTF-8\"></head>"
            + "<body style=\"margin:0; padding:0; background:#F0F4F8; font-family:Arial, sans-serif;\">"
            + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#F0F4F8; padding:32px 0;\">"
            + "<tr><td align=\"center\">"
            + "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#FFF; border-radius:12px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.08);\">"
            + "<tr><td style=\"background:#043C5C; padding:28px 32px; text-align:center;\">"
            + "<h1 style=\"margin:0; font-size:26px; font-weight:900; color:#FFF; letter-spacing:1px;\">CENTRE MANARA</h1>"
            + "<p style=\"margin:6px 0 0; font-size:13px; color:#E8BB08; font-weight:600;\">Activites pour enfants</p>"
            + "</td></tr>"
            + "<tr><td style=\"background:#E8BB08; height:4px;\"></td></tr>"
            + "<tr><td style=\"padding:32px;\">"
            + "<h2 style=\"margin:0 0 20px; font-size:22px; color:#043C5C; font-weight:800;\">" + titre + "</h2>"
            + "<div style=\"font-size:15px; color:#374151; line-height:1.7;\">" + contenu + "</div>"
            + "</td></tr>"
            + "<tr><td style=\"background:#F9FAFB; padding:20px 32px; text-align:center; border-top:1px solid #E5E7EB;\">"
            + "<p style=\"margin:0; font-size:13px; color:#9CA3AF;\">Centre Manara &mdash; Activites et loisirs pour enfants</p>"
            + "<p style=\"margin:6px 0 0; font-size:12px; color:#D1D5DB;\">Message automatique, ne pas repondre.</p>"
            + "</td></tr>"
            + "</table></td></tr></table></body></html>";
    }
}
