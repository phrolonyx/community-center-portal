package com.manara.backend.repository;

import com.manara.backend.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
 * Repository pour acceder aux messages dans la BD.
 * Les requetes JPQL permettent de filtrer par destinataire.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {

    // cherche les messages envoyes a ce parent OU envoyes a tout le monde (recipient null)
    @Query("SELECT m FROM Message m WHERE m.recipient.id = :userId OR m.recipient IS NULL ORDER BY m.createdAt DESC")
    List<Message> findByRecipientIdOrGlobal(Integer userId);

    // compte les messages non lus pour afficher le badge
    @Query("SELECT COUNT(m) FROM Message m WHERE (m.recipient.id = :userId OR m.recipient IS NULL) AND m.isRead = false")
    long countUnreadByUserId(Integer userId);

    // tous les messages tries par date (pour la page admin)
    List<Message> findAllByOrderByCreatedAtDesc();
}
