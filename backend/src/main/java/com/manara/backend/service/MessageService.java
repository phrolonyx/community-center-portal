package com.manara.backend.service;

import com.manara.backend.model.Message;
import com.manara.backend.model.User;
import com.manara.backend.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/*
 * Gestion des messages entre admin et parents.
 * L'admin peut envoyer a un parent ou a tous.
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

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

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }
}
