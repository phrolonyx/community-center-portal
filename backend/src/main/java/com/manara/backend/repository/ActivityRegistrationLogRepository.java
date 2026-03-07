package com.manara.backend.repository;

import com.manara.backend.model.ActivityRegistrationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRegistrationLogRepository extends JpaRepository<ActivityRegistrationLog, Integer> {
}
