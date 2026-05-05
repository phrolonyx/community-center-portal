package com.manara.backend.controller.rest;

import com.manara.backend.dto.MessageResponseDto;
import com.manara.backend.model.security.CustomUserDetails;
import com.manara.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controller pour la réception des messages côté parent
@RestController
@RequestMapping("/parent/messages")
@PreAuthorize("hasAuthority('PARENT')")
public class ParentMessageController {

    @Autowired
    private MessageService messageService;

    // Récupérer tous les messages du parent connecté (perso + globaux)
    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> getMesMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer userId = userDetails.getUser().getId();
        List<MessageResponseDto> dtos = messageService.getMessagesForUser(userId).stream()
                .map(MessageResponseDto::from)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Compteur de messages non lus pour le badge
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> countUnread(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        long count = messageService.countUnread(userDetails.getUser().getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    // Marquer un message comme lu
    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> marquerLu(@PathVariable Integer id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok(Map.of("message", "Message marqué comme lu"));
    }

    // Supprimer un message (seulement si c'est un message personnel du parent)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> supprimer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer id) {
        try {
            messageService.deleteForUser(id, userDetails.getUser().getId());
            return ResponseEntity.ok(Map.of("message", "Message supprimé"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }
}
