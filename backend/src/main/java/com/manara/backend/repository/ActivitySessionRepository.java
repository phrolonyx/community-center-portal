package com.manara.backend.repository;

import com.manara.backend.model.Activity;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.Location;
import com.manara.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ActivitySessionRepository extends JpaRepository<ActivitySession, Integer> {
    Optional<ActivitySession> findActivitySessionByActivity(Activity activity);

    Optional<ActivitySession> findActivitySessionByLocation(Location location);

    Optional<ActivitySession> findActivitySessionByHost(User host);
}
