package com.manara.backend.repository;

import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParentProfileRepository extends JpaRepository<ParentProfile, Integer> {
    Optional<ParentProfile> findByUser(User user);
}
