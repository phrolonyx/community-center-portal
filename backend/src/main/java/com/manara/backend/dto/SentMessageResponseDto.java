package com.manara.backend.dto;

import com.manara.backend.model.Message;
import lombok.Data;

import java.time.LocalDateTime;

// DTO pour l'historique des messages envoyes (cote admin/animateur)
@Data
public class SentMessageResponseDto {

    private Integer id;
    private String subject;
    private String content;
    private String type;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private String recipientName; // "Tous les parents" si broadcast, ou liste de noms si grouping
    private Integer recipientCount; // nombre de destinataires (1 = ciblé, 0 = broadcast global)

    public static SentMessageResponseDto from(Message m) {
        SentMessageResponseDto dto = new SentMessageResponseDto();
        dto.setId(m.getId());
        dto.setSubject(m.getSubject());
        dto.setContent(m.getContent());
        dto.setType(m.getType());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setIsRead(m.getIsRead());

        if (m.getRecipient() == null) {
            dto.setRecipientName("Tous les parents");
            dto.setRecipientCount(0); // 0 = broadcast global
        } else {
            dto.setRecipientName(m.getRecipient().getFirstName() + " " + m.getRecipient().getLastName());
            dto.setRecipientCount(1);
        }

        return dto;
    }
}
