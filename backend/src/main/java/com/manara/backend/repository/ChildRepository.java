package com.manara.backend.repository;

import com.manara.backend.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Integer> {

    List<Child> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName,
            String lastName
    );
    // Recherche par prénom/nom du parent (via ChildParent → ParentProfile → User)
    List<Child> findByParentLinks_ParentProfile_User_FirstNameContainingIgnoreCase(String firstName);

    List<Child> findByParentLinks_ParentProfile_User_LastNameContainingIgnoreCase(String lastName);

}



