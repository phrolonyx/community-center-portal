package com.manara.backend.dto;

import com.manara.backend.model.Message;
import lombok.Data;

import java.time.LocalDateTime;

// DTO pour renvoyer un message au frontend (parent)
@Data
public class MessageResponseDto {

    private Integer id;
    private String subject;
    private String content;
    private Boolean isRead;
    private String type;
    private String senderInitials;
    private Sender sender;
    private LocalDateTime createdAt;

    @Data
    public static class Sender {
        private String fullName;
    }

    public static MessageResponseDto from(Message m) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(m.getId());
        dto.setSubject(m.getSubject());
        dto.setContent(m.getContent());
        dto.setIsRead(m.getIsRead());
        dto.setType(m.getType());
        dto.setCreatedAt(m.getCreatedAt());
        dto.setSenderInitials(m.getSenderInitials());

        Sender s = new Sender();
        s.setFullName(m.getSender().getFirstName() + " " + m.getSender().getLastName());
        dto.setSender(s);

        return dto;
    }
}
