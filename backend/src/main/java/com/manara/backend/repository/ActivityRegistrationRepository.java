package com.manara.backend.repository;

import com.manara.backend.model.ActivityRegistration;
import com.manara.backend.model.ActivitySession;
import com.manara.backend.model.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityRegistrationRepository extends JpaRepository<ActivityRegistration, Integer> {

    Optional<ActivityRegistration> findActivityRegistrationByActivitySession(ActivitySession activitySession);

    Optional<ActivityRegistration> findActivityRegistrationByParentProfile(ParentProfile parentProfile);

}
