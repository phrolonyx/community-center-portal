package com.manara.backend.service;

import com.manara.backend.dto.SentMessageResponseDto;
import com.manara.backend.model.Message;
import com.manara.backend.model.MessageDeletion;
import com.manara.backend.model.User;
import com.manara.backend.repository.MessageDeletionRepository;
import com.manara.backend.repository.MessageRepository;
import com.manara.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service de messagerie interne (admin / animateur / parent).
 * Gere l'envoi, la reception, le marquage comme lu, la suppression cote user
 * et l'historique des envois regroupes.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageDeletionRepository messageDeletionRepository;

    @Autowired
    private UserRepository userRepository;

    // ========================================================================
    // ENVOI
    // ========================================================================

    // Cree et sauvegarde un nouveau message individuel
    public void sendMessage(User sender, User recipient, String subject, String content, String type) {
        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setSubject(subject);
        message.setContent(content);
        message.setType(type);
        message.setCreatedAt(LocalDateTime.now());
        message.setIsRead(false);
        messageRepository.save(message);
    }

    // Broadcast a tous les parents : un seul message avec recipient = null
    public void sendToAll(User sender, String subject, String content, String type) {
        sendMessage(sender, null, subject, content, type);
    }

    // Envoi a plusieurs destinataires : une copie individuelle par user.
    // Tous les messages du batch partagent le MEME createdAt pour permettre
    // le regroupement dans l'historique des envois.
    public void sendToMultipleUsers(User sender, List<User> recipients, String subject, String content, String type) {
        LocalDateTime now = LocalDateTime.now();
        for (User r : recipients) {
            Message m = new Message();
            m.setSender(sender);
            m.setRecipient(r);
            m.setSubject(subject);
            m.setContent(content);
            m.setType(type);
            m.setCreatedAt(now);
            m.setIsRead(false);
            messageRepository.save(m);
        }
    }

    // ========================================================================
    // RECEPTION (parent)
    // ========================================================================

    // Messages du parent : perso + broadcasts envoyes APRES sa date d'inscription
    public List<Message> getMessagesForUser(Integer userId) {
        LocalDateTime userCreatedAt = userRepository.findById(userId)
                .map(User::getCreatedAt)
                .orElse(LocalDateTime.now());
        return messageRepository.findByRecipientIdOrGlobal(userId, userCreatedAt);
    }

    public long countUnread(Integer userId) {
        LocalDateTime userCreatedAt = userRepository.findById(userId)
                .map(User::getCreatedAt)
                .orElse(LocalDateTime.now());
        return messageRepository.countUnreadByUserId(userId, userCreatedAt);
    }

    // ========================================================================
    // RECEPTION (admin / animateur) — uniquement les messages directs (pas les broadcasts)
    // ========================================================================

    public List<Message> getDirectMessagesForUser(Integer userId) {
        return messageRepository.findDirectByRecipientId(userId);
    }

    public long countUnreadDirect(Integer userId) {
        return messageRepository.countUnreadDirectByUserId(userId);
    }

    // ========================================================================
    // ACTIONS SUR UN MESSAGE
    // ========================================================================

    public void markAsRead(Integer messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setIsRead(true);
        messageRepository.save(message);
    }

    // Suppression cote user : on cache le message pour ce user seulement.
    // Le message reste en BD pour ne pas affecter les autres destinataires.
    public void deleteForUser(Integer messageId, Integer userId) {
        if (messageDeletionRepository.findByUserIdAndMessageId(userId, messageId).isPresent()) {
            return;
        }
        messageRepository.findById(messageId).orElseThrow();
        MessageDeletion deletion = new MessageDeletion();
        deletion.setUserId(userId);
        deletion.setMessageId(messageId);
        deletion.setHiddenAt(LocalDateTime.now());
        messageDeletionRepository.save(deletion);
    }

    // ========================================================================
    // HISTORIQUE DES ENVOIS
    // ========================================================================

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }

    // Historique regroupe : les envois multi-destinataires (Plusieurs parents,
    // Tous les animateurs) sont fusionnes en un seul item via sujet + contenu
    // + type + createdAt. Les broadcasts globaux restent individuels.
    public List<SentMessageResponseDto> getSentByUserGrouped(Integer senderId) {
        List<Message> messages = messageRepository.findBySenderIdOrderByCreatedAtDesc(senderId);

        Map<String, List<Message>> groups = new LinkedHashMap<>();
        for (Message m : messages) {
            String key = (m.getRecipient() == null)
                    ? "global-" + m.getId()
                    : m.getCreatedAt() + "|" + m.getSubject() + "|" + m.getContent() + "|" + m.getType();
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(m);
        }

        List<SentMessageResponseDto> dtos = new ArrayList<>();
        for (List<Message> group : groups.values()) {
            Message first = group.getFirst();
            SentMessageResponseDto dto = SentMessageResponseDto.from(first);

            if (first.getRecipient() != null && group.size() > 1) {
                String names = group.stream()
                        .map(m -> m.getRecipient().getFirstName() + " " + m.getRecipient().getLastName())
                        .collect(Collectors.joining(", "));
                dto.setRecipientName(names);
                dto.setRecipientCount(group.size());
            }
            dtos.add(dto);
        }
        return dtos;
    }
}
