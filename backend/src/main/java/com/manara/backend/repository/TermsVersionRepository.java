package com.manara.backend.repository;

import com.manara.backend.model.TermsVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermsVersionRepository extends JpaRepository<TermsVersion, Integer> {
}
