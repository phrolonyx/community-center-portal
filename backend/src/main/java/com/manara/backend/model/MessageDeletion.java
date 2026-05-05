package com.manara.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/*
 * Trace les messages cachés par un parent.
 * Quand un parent "supprime" un message global, on cree une ligne ici
 * au lieu de supprimer le message en BD. Comme ca les autres parents le voient encore.
 */
@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "message_id"}))
public class MessageDeletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "message_id", nullable = false)
    private Integer messageId;

    @Column(nullable = false)
    private LocalDateTime hiddenAt;
}
