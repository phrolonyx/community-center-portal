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

// Controller pour la boite de reception admin (recoit les messages des autres admins)
@RestController
@RequestMapping("/admin/inbox")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminInboxController {

    @Autowired
    private MessageService messageService;

    // Recuperer les messages directs adresses a cet admin (pas les broadcasts parents)
    @GetMapping
    public ResponseEntity<List<MessageResponseDto>> getMesMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUser().getId();
        List<MessageResponseDto> dtos = messageService.getDirectMessagesForUser(userId).stream()
                .map(MessageResponseDto::from)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // Compteur non lus pour le badge
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> countUnread(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        long count = messageService.countUnreadDirect(userDetails.getUser().getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    // Marquer comme lu
    @PutMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> marquerLu(@PathVariable Integer id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok(Map.of("message", "Message marqué comme lu"));
    }

    // Suppression cote admin (cache pour ce user seulement)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> supprimer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer id) {
        messageService.deleteForUser(id, userDetails.getUser().getId());
        return ResponseEntity.ok(Map.of("message", "Message supprimé"));
    }
}
