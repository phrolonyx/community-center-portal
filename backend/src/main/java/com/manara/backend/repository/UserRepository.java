package com.manara.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRole_Name(String roleName);
    List<User> findByEnabled(Boolean enabled);
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
}
