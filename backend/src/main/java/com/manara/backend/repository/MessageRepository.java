package com.manara.backend.repository;

import com.manara.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
 * Repository pour acceder aux messages dans la BD.
 * Les requetes JPQL excluent les messages que le parent a cache de son cote
 * (table MessageDeletion).
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // messages envoyes a ce parent OU envoyes a tout le monde, en excluant ceux qu'il a caches
    @Query("SELECT m FROM Message m " +
           "WHERE (m.recipient.id = :userId OR m.recipient IS NULL) " +
           "AND m.id NOT IN (SELECT d.messageId FROM MessageDeletion d WHERE d.userId = :userId) " +
           "ORDER BY m.createdAt DESC")
    List<Message> findByRecipientIdOrGlobal(Integer userId);

    // compte les messages non lus pour le badge, en excluant les caches
    @Query("SELECT COUNT(m) FROM Message m " +
           "WHERE (m.recipient.id = :userId OR m.recipient IS NULL) " +
           "AND m.isRead = false " +
           "AND m.id NOT IN (SELECT d.messageId FROM MessageDeletion d WHERE d.userId = :userId)")
    long countUnreadByUserId(Integer userId);

    // tous les messages tries par date (pour la page admin)
    List<Message> findAllByOrderByCreatedAtDesc();

    // historique des messages envoyes par un user (admin ou animateur)
    List<Message> findBySenderIdOrderByCreatedAtDesc(Integer senderId);

    // Messages adresses uniquement a cet user (pas les broadcasts) — utile pour la inbox admin
    @Query("SELECT m FROM Message m " +
           "WHERE m.recipient.id = :userId " +
           "AND m.id NOT IN (SELECT d.messageId FROM MessageDeletion d WHERE d.userId = :userId) " +
           "ORDER BY m.createdAt DESC")
    List<Message> findDirectByRecipientId(Integer userId);

    @Query("SELECT COUNT(m) FROM Message m " +
           "WHERE m.recipient.id = :userId AND m.isRead = false " +
           "AND m.id NOT IN (SELECT d.messageId FROM MessageDeletion d WHERE d.userId = :userId)")
    long countUnreadDirectByUserId(Integer userId);
}
