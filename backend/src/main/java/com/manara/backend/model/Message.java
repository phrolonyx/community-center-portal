package com.manara.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/*
 * Table pour les messages/notifications envoyes par l'admin aux parents.
 * Si recipient est null, ca veut dire que c'est envoye a tous les parents.
 * type peut etre : ANNONCE, RAPPEL ou ANNULATION
 */
@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // l'admin qui a envoye le message
    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;

    // null = message envoye a tout le monde
    @ManyToOne
    private User recipient;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isRead = false;

    // ANNONCE, RAPPEL ou ANNULATION
    @Column(nullable = false, length = 50)
    private String type;

    public String getSenderInitials() {
        return (sender.getFirstName().substring(0, 1) + sender.getLastName().substring(0, 1)).toUpperCase();
    }
}
