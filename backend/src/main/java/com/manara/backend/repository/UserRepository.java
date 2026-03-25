package com.manara.backend.repository;

import com.manara.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/*
 * Repository pour acceder aux utilisateurs dans la BD.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole_Name(String roleName);
    List<User> findByEnabled(Boolean enabled);
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String firstName, String lastName, String email);
    long countByRoleName(String roleName);
    long countByEnabledFalse();
    List<User> findByEnabledFalse();
    User getUserByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByPendingTrue();
}
