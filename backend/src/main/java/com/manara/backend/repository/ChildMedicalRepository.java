package com.manara.backend.repository;

import com.manara.backend.model.ChildMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildMedicalRepository extends JpaRepository<ChildMedical, Integer> {
}
