package com.manara.backend.repository;

import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Integer> {

    Optional<ActivityRegistration> findActivityRegistrationByActivitySession(ActivitySession activitySession);
    Optional<ActivityRegistration> findActivityRegistrationByParentProfile(ParentProfile parentProfile);

    List<ActivityRegistration> findByParentProfileId(Integer parentProfileId);
    List<ActivityRegistration> findByChildId(Integer childId);

    boolean existsByChildIdAndActivitySessionIdAndStatusNot(Integer childId, Integer sessionId, String status);

    // Inscriptions liees a une seance (utile pour annulation seance)
    List<ActivityRegistration> findByActivitySessionId(Integer sessionId);

    // Nombre d'inscriptions avec un statut donne pour une seance (compteur capacite)
    long countByActivitySessionIdAndStatus(Integer sessionId, String status);
}
