package com.manara.backend.repository;

import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Repository pour acceder aux inscriptions dans la BD.
 */
@Repository
public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Integer> {

    // Methodes existantes (collegues)
    Optional<ActivityRegistration> findActivityRegistrationByActivitySession(ActivitySession activitySession);
    Optional<ActivityRegistration> findActivityRegistrationByParentProfile(ParentProfile parentProfile);

    // Nos ajouts : chercher les inscriptions par parent ou par enfant
    List<ActivityRegistration> findByParentProfileId(Integer parentProfileId);
    List<ActivityRegistration> findByChildId(Integer childId);
}
