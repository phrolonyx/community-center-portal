package com.manara.backend.controller.rest;

import com.manara.backend.dto.SentMessageResponseDto;
import com.manara.backend.model.User;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.MessageService;
import com.manara.backend.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controller pour l'envoi de messages depuis l'admin (vers parents OU animateurs)
@RestController
@RequestMapping("/admin/messages")
@PreAuthorize("hasAuthority('ADMIN')")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> envoyer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MessageRequest body) {

        if (body.getSujet() == null || body.getSujet().isBlank()
                || body.getContenu() == null || body.getContenu().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Sujet et contenu obligatoires"));
        }

        User sender = userDetails.getUser();
        String type = body.getType() != null ? body.getType() : "ANNONCE";
        String cible = body.getCible() != null ? body.getCible() : "tous-parents";

        switch (cible) {
            case "tous-parents" -> {
                // Bouton pour envoi à tous les parents
                messageService.sendToAll(sender, body.getSujet(), body.getContenu(), type);
                return ResponseEntity.ok(Map.of("message", "Message envoyé à tous les parents"));
            }
            case "tous-animateurs" -> {
                // Bouton pour envoi à tous les animateurs
                List<User> animateurs = userService.findByRoleName("ANIMATEUR");
                messageService.sendToMultipleUsers(sender, animateurs, body.getSujet(), body.getContenu(), type);
                return ResponseEntity.ok(Map.of("message", "Message envoyé à tous les animateurs"));
            }
            case "parent", "animateur" -> {
                // Envoi ciblé par ID destinataire
                if (body.getRecipientId() == null) {
                    return ResponseEntity.badRequest().body(Map.of("error", "ID destinataire obligatoire"));
                }
                User recipient = userService.findById(body.getRecipientId());
                messageService.sendMessage(sender, recipient, body.getSujet(), body.getContenu(), type);
                return ResponseEntity.ok(Map.of("message", "Message envoyé à " + recipient.getFirstName()));
            }
            case "plusieurs-parents" -> {
                // Envoi ciblé à plusieurs parents
                if (body.getRecipientIds() == null || body.getRecipientIds().isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Au moins un parent doit être sélectionné"));
                }
                List<User> recipients = body.getRecipientIds().stream()
                        .map(userService::findById)
                        .toList();
                messageService.sendToMultipleUsers(sender, recipients, body.getSujet(), body.getContenu(), type);
                return ResponseEntity.ok(Map.of("message", "Message envoyé à " + recipients.size() + " parent(s)"));
            }
            default -> {
                return ResponseEntity.badRequest().body(Map.of("error", "Cible inconnue : " + cible));
            }
        }
    }

    // Historique des messages envoyes par cet admin (regroupes par envoi)
    @GetMapping("/sent")
    public ResponseEntity<List<SentMessageResponseDto>> getEnvoyes(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUser().getId();
        return ResponseEntity.ok(messageService.getSentByUserGrouped(userId));
    }

    @Data
    public static class MessageRequest {
        private String sujet;
        private String contenu;
        private Integer recipientId;
        private List<Integer> recipientIds; // pour "plusieurs-parents"
        private String type;
        private String cible; // tous-parents | tous-animateurs | parent | animateur | plusieurs-parents
    }
}
