package com.manara.backend.repository;

import com.manara.backend.model.MessageDeletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageDeletionRepository extends JpaRepository<MessageDeletion, Integer> {

    // verifie si un parent a deja cache ce message
    Optional<MessageDeletion> findByUserIdAndMessageId(Integer userId, Integer messageId);
}
