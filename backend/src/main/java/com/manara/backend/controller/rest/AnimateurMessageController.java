package com.manara.backend.controller.rest;

import com.manara.backend.dto.SentMessageResponseDto;
import com.manara.backend.dto.UserResponseDto;
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

// Controller d'envoi de messages cote animateur (vers parents OU admins)
@RestController
@RequestMapping("/animateur/messages")
@PreAuthorize("hasAuthority('ANIMATEUR')")
public class AnimateurMessageController {

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
            case "tous-admins" -> {
                // Envoi à tous les admins (copie individuelle par admin)
                List<User> admins = userService.findByRoleName("ADMIN");
                messageService.sendToMultipleUsers(sender, admins, body.getSujet(), body.getContenu(), type);
                return ResponseEntity.ok(Map.of("message", "Message envoyé à tous les admins"));
            }
            case "parent", "admin" -> {
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

    // Liste des parents (pour le select dans le formulaire animateur)
    @GetMapping("/parents")
    public ResponseEntity<List<UserResponseDto>> listParents() {
        List<UserResponseDto> dtos = userService.findByRoleName("PARENT").stream()
                .map(UserResponseDto::from)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Liste des admins (pour le select dans le formulaire animateur)
    @GetMapping("/admins")
    public ResponseEntity<List<UserResponseDto>> listAdmins() {
        List<UserResponseDto> dtos = userService.findByRoleName("ADMIN").stream()
                .map(UserResponseDto::from)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Historique des messages envoyes par cet animateur (regroupes par envoi)
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
        private String cible; // tous-parents | tous-admins | parent | admin | plusieurs-parents
    }
}
