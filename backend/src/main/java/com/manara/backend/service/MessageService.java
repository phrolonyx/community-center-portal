package com.manara.backend.service;

import com.manara.backend.dto.SentMessageResponseDto;
import com.manara.backend.model.Message;
import com.manara.backend.model.MessageDeletion;
import com.manara.backend.model.User;
import com.manara.backend.repository.MessageDeletionRepository;
import com.manara.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Gestion des messages entre admin et parents.
 * L'admin peut envoyer a un parent ou a tous.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageDeletionRepository messageDeletionRepository;

    // recupere les messages du parent (perso + ceux envoyes a tout le monde)
    public List<Message> getMessagesForUser(Integer userId) {
        return messageRepository.findByRecipientIdOrGlobal(userId);
    }

    public long countUnread(Integer userId) {
        return messageRepository.countUnreadByUserId(userId);
    }

    public void markAsRead(Integer messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow();
        message.setIsRead(true);
        messageRepository.save(message);
    }

    // cree et sauvegarde un nouveau message
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

    public void sendToAll(User sender, String subject, String content, String type) {
        sendMessage(sender, null, subject, content, type);
    }

    // Envoi a plusieurs users : copie individuelle par destinataire
    // Tous les messages partagent le MEME createdAt pour pouvoir les regrouper après
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

    // Recupere les messages adresses directement a cet utilisateur (sans les broadcasts globaux)
    public List<Message> getDirectMessagesForUser(Integer userId) {
        return messageRepository.findDirectByRecipientId(userId);
    }

    public long countUnreadDirect(Integer userId) {
        return messageRepository.countUnreadDirectByUserId(userId);
    }

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }

    // Historique des messages envoyes par cet utilisateur
    public List<Message> getSentByUser(Integer senderId) {
        return messageRepository.findBySenderIdOrderByCreatedAtDesc(senderId);
    }

    // Historique regroupé : les envois multi-destinataires sont fusionnés en un seul item
    // Clé de regroupement : sujet + contenu + type + createdAt (les broadcasts globaux restent individuels)
    public List<SentMessageResponseDto> getSentByUserGrouped(Integer senderId) {
        List<Message> messages = messageRepository.findBySenderIdOrderByCreatedAtDesc(senderId);

        // Regroupe les messages individuels qui ont le même sujet/contenu/createdAt (envoi groupé)
        Map<String, List<Message>> groups = new LinkedHashMap<>();
        for (Message m : messages) {
            String key;
            if (m.getRecipient() == null) {
                // Broadcast global : on ne regroupe pas
                key = "global-" + m.getId();
            } else {
                key = m.getCreatedAt() + "|" + m.getSubject() + "|" + m.getContent() + "|" + m.getType();
            }
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(m);
        }

        // Convertit chaque groupe en DTO
        List<SentMessageResponseDto> dtos = new ArrayList<>();
        for (List<Message> group : groups.values()) {
            Message first = group.getFirst();
            SentMessageResponseDto dto = SentMessageResponseDto.from(first);

            if (first.getRecipient() != null && group.size() > 1) {
                // Plusieurs destinataires : on concatène les noms
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

    // Suppression "côté parent" : on cache le message pour ce user seulement,
    // les autres parents continuent à le voir (utile pour les messages globaux)
    public void deleteForUser(Integer messageId, Integer userId) {
        // si déjà caché, on ne fait rien
        if (messageDeletionRepository.findByUserIdAndMessageId(userId, messageId).isPresent()) {
            return;
        }
        // verifier que le message existe
        messageRepository.findById(messageId).orElseThrow();

        MessageDeletion deletion = new MessageDeletion();
        deletion.setUserId(userId);
        deletion.setMessageId(messageId);
        deletion.setHiddenAt(LocalDateTime.now());
        messageDeletionRepository.save(deletion);
    }
}
