package com.manara.backend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service d'envoi de courriels (Gmail SMTP).
 * Tous les emails utilisent un template HTML aux couleurs du Centre Communautaire
 * (bleu #043C5C + barre jaune #E8BB08).
 *
 * Les methodes ne throw jamais : si SMTP plante, l'erreur est loguee mais
 * l'operation metier (inscription, confirmation, etc.) continue.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    // Envoi technique d'un courriel HTML
    public void envoyerEmail(String destinataire, String sujet, String contenuHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(destinataire);
            helper.setSubject(sujet);
            helper.setText(contenuHtml, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Erreur envoi courriel à " + destinataire + " : " + e.getMessage());
        }
    }

    // Bienvenue : compte créé, en attente de validation par l'admin
    public void envoyerBienvenue(String email, String prenom) {
        String html = construireTemplate(
            "Bienvenue, " + prenom + " !",
            "<p>Merci de vous être inscrit au <strong>Centre Communautaire</strong>.</p>"
            + "<p>Votre compte a bien été créé. Avant de pouvoir vous connecter, "
            + "<strong>un administrateur doit valider votre profil</strong>.</p>"
            + "<p>Vous recevrez un courriel de confirmation dès que votre compte sera approuvé.</p>"
            + "<p>Merci de votre patience !</p>"
        );
        envoyerEmail(email, "Inscription reçue — Centre Communautaire", html);
    }

    // Compte approuvé par l'admin
    public void envoyerCompteApprouve(String email, String prenom) {
        String html = construireTemplate(
            "Votre compte est activé !",
            "<p>Bonjour " + prenom + ",</p>"
            + "<p>Bonne nouvelle : votre compte au <strong>Centre Communautaire</strong> "
            + "vient d'être <strong>approuvé</strong> par notre équipe.</p>"
            + "<p>Vous pouvez maintenant vous connecter et :</p>"
            + "<ul>"
            + "  <li>Ajouter vos enfants à votre profil</li>"
            + "  <li>Les inscrire aux activités disponibles</li>"
            + "  <li>Suivre leurs inscriptions en temps réel</li>"
            + "</ul>"
            + "<p>À bientôt au Centre Communautaire !</p>"
        );
        envoyerEmail(email, "Compte approuvé — Centre Communautaire", html);
    }

    // Confirmation lorsque le parent inscrit son enfant à une activité
    public void envoyerConfirmationInscription(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance) {
        String html = construireTemplate(
            "Inscription enregistrée !",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>L'inscription de <strong>" + nomEnfant + "</strong> à l'activité "
            + "<strong>" + nomActivite + "</strong> a bien été enregistrée.</p>"
            + ligneInfo("Enfant", nomEnfant, true)
            + ligneInfo("Activité", nomActivite, false)
            + ligneInfo("Date", dateSeance, true)
            + ligneInfo("Statut", "En attente", false)
            + "<p>Vous recevrez une notification lorsque l'inscription sera confirmée.</p>"
        );
        envoyerEmail(email, "Inscription enregistrée — " + nomActivite, html);
    }

    // Confirmation par l'admin (avec lieu + animateur)
    public void envoyerConfirmationAdmin(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance, String lieu, String nomAnimateur) {
        String html = construireTemplate(
            "Inscription confirmée !",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>Bonne nouvelle ! L'inscription de <strong>" + nomEnfant + "</strong> "
            + "à l'activité <strong>" + nomActivite + "</strong> a été <strong>confirmée</strong> par l'administration.</p>"
            + ligneInfo("Activité", nomActivite, true)
            + ligneInfo("Date", dateSeance, false)
            + ligneInfo("Lieu", lieu != null ? lieu : "À confirmer", true)
            + ligneInfo("Animateur", nomAnimateur != null ? nomAnimateur : "À confirmer", false)
            + "<p>À bientôt au Centre Communautaire !</p>"
        );
        envoyerEmail(email, "Inscription confirmée — " + nomActivite, html);
    }

    // Annulation d'une inscription par l'admin (motif optionnel affiché dans un encadré)
    public void envoyerAnnulation(String email, String prenomParent,
            String nomEnfant, String nomActivite, String motif) {
        StringBuilder contenu = new StringBuilder();
        contenu.append("<p>Bonjour ").append(prenomParent).append(",</p>");
        contenu.append("<p>L'inscription de <strong>").append(nomEnfant).append("</strong> à l'activité ");
        contenu.append("<strong>").append(nomActivite).append("</strong> a été annulée par l'administration.</p>");

        if (motif != null && !motif.isBlank()) {
            contenu.append("<p style=\"padding:12px 16px; background:#FFF3CD; border-left:4px solid #E8BB08; border-radius:4px;\">");
            contenu.append("<strong>Motif d'annulation :</strong><br>").append(motif);
            contenu.append("</p>");
        }

        contenu.append("<p>Pour toute question, n'hésitez pas à contacter l'administration du Centre Communautaire.</p>");

        envoyerEmail(email, "Annulation — " + nomActivite,
                construireTemplate("Inscription annulée", contenu.toString()));
    }

    // Annulation d'une séance entière (envoyé à chaque parent inscrit)
    public void envoyerAnnulationSeance(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance) {
        String html = construireTemplate(
            "Séance annulée",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>Nous sommes désolés de vous informer que la séance "
            + "<strong>" + nomActivite + "</strong> du <strong>" + dateSeance + "</strong> "
            + "à laquelle <strong>" + nomEnfant + "</strong> était inscrit a été annulée.</p>"
            + "<p>Pour plus d'informations, contactez l'administration du Centre Communautaire.</p>"
        );
        envoyerEmail(email, "Séance annulée — " + nomActivite, html);
    }

    // Rappel 24h avant la séance
    public void envoyerRappel24h(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance, String lieu) {
        String html = construireTemplate(
            "Rappel : activité demain",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>Petit rappel : <strong>" + nomEnfant + "</strong> participe demain à "
            + "<strong>" + nomActivite + "</strong>.</p>"
            + ligneInfo("Activité", nomActivite, true)
            + ligneInfo("Date", dateSeance, false)
            + ligneInfo("Lieu", lieu, true)
            + "<p>À demain au Centre Communautaire !</p>"
        );
        envoyerEmail(email, "Rappel demain — " + nomActivite, html);
    }

    // Rappel 2h avant la séance
    public void envoyerRappel2h(String email, String prenomParent,
            String nomEnfant, String nomActivite, String dateSeance, String lieu) {
        String html = construireTemplate(
            "Rappel : activité dans 2 heures !",
            "<p>Bonjour " + prenomParent + ",</p>"
            + "<p>L'activité <strong>" + nomActivite + "</strong> commence dans <strong>2 heures</strong>.</p>"
            + "<p><strong>" + nomEnfant + "</strong> est attendu à <strong>" + dateSeance + "</strong> "
            + "au lieu suivant : <strong>" + lieu + "</strong>.</p>"
            + "<p>À tout de suite au Centre Communautaire !</p>"
        );
        envoyerEmail(email, "Activité dans 2h — " + nomActivite, html);
    }

    // Helper : ligne d'information formatée pour les tableaux du courriel.
    // alterne sert à alterner les fonds clair/blanc des lignes pour la lisibilité.
    private String ligneInfo(String label, String valeur, boolean alterne) {
        String bg = alterne ? "background:#F0F4F8;" : "";
        return "<table style=\"width:100%; border-collapse:collapse;\"><tr>"
                + "<td style=\"padding:10px 14px; " + bg + " font-weight:700; color:#043C5C; width:30%;\">" + label + "</td>"
                + "<td style=\"padding:10px 14px; " + bg + "\">" + valeur + "</td>"
                + "</tr></table>";
    }

    // Génère le template HTML commun (header bleu + barre jaune + footer)
    private String construireTemplate(String titre, String contenu) {
        return "<!DOCTYPE html>"
            + "<html><head><meta charset=\"UTF-8\"></head>"
            + "<body style=\"margin:0; padding:0; background:#F0F4F8; font-family:Arial, sans-serif;\">"
            + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#F0F4F8; padding:32px 0;\">"
            + "<tr><td align=\"center\">"
            + "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#FFF; border-radius:12px; overflow:hidden; box-shadow:0 2px 8px rgba(0,0,0,0.08);\">"
            + "<tr><td style=\"background:#043C5C; padding:28px 32px; text-align:center;\">"
            + "<h1 style=\"margin:0; font-size:26px; font-weight:900; color:#FFF; letter-spacing:1px;\">CENTRE COMMUNAUTAIRE</h1>"
            + "<p style=\"margin:6px 0 0; font-size:13px; color:#E8BB08; font-weight:600;\">Activités pour enfants</p>"
            + "</td></tr>"
            + "<tr><td style=\"background:#E8BB08; height:4px;\"></td></tr>"
            + "<tr><td style=\"padding:32px;\">"
            + "<h2 style=\"margin:0 0 20px; font-size:22px; color:#043C5C; font-weight:800;\">" + titre + "</h2>"
            + "<div style=\"font-size:15px; color:#374151; line-height:1.7;\">" + contenu + "</div>"
            + "</td></tr>"
            + "<tr><td style=\"background:#F9FAFB; padding:20px 32px; text-align:center; border-top:1px solid #E5E7EB;\">"
            + "<p style=\"margin:0; font-size:13px; color:#9CA3AF;\">Centre Communautaire &mdash; Activités et loisirs pour enfants</p>"
            + "<p style=\"margin:6px 0 0; font-size:12px; color:#D1D5DB;\">Message automatique, ne pas répondre.</p>"
            + "</td></tr>"
            + "</table></td></tr></table></body></html>";
    }
}
