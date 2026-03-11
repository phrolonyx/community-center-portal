package com.manara.backend.repository;

import com.manara.backend.model.ChildParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildParentRepository extends JpaRepository<ChildParent, Integer> {
}
