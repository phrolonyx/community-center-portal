package com.manara.backend.service;

import com.manara.backend.model.*;
import com.manara.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;
    @Autowired
    private ChildParentRepository childParentRepository;
    @Autowired
    private ParentProfileRepository parentProfileRepository;
    @Autowired
    private ChildMedicalRepository childMedicalRepository;

    @Autowired
    private ActivityRegistrationRepository activityRegistrationRepository;

    @Transactional
    public void createChild(Child child, Integer parentProfileId) {
        ParentProfile parentProfile = parentProfileRepository.findById(parentProfileId).orElseThrow();
        ChildParent childParent = new ChildParent(child, parentProfile);
        childRepository.save(child);
        childParentRepository.save(childParent);
    }



    public Child findById(Integer id) {
        return childRepository.findById(id).orElseThrow();
    }

    public List<Child> findByParentProfileId(Integer parentProfileId) {
       return childParentRepository
               .findChildParentsByParentProfile_Id(parentProfileId)
               .stream()
               .map(ChildParent::getChild)
               .toList();
    }

    @Transactional
    public void updateChild(Child child) {
        childRepository.save(child);
    }

    @Transactional
    public void addMedicalCondition(ChildMedical condition, Integer childId) {
        Child child = childRepository.findById(childId).orElseThrow();
        condition.setChild(child);
        childMedicalRepository.save(condition);
    }

    @Transactional
    public void updateMedicalCondition(ChildMedical condition) {
        childMedicalRepository.save(condition);
    }

    @Transactional
    public void deleteMedicalCondition(Integer conditionId) {
        childMedicalRepository.deleteById(conditionId);
    }


    public List<Child> searchByName(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return childRepository.findAll();
        }

        keyword = keyword.trim();

        // Resultats enfant
        List<Child> results = childRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);

        // Resultats parent (prénom)
        List<Child> parentFirst = childRepository
                .findByParentLinks_ParentProfile_User_FirstNameContainingIgnoreCase(keyword);

        // Resultats parent (nom)
        List<Child> parentLast = childRepository
                .findByParentLinks_ParentProfile_User_LastNameContainingIgnoreCase(keyword);

        // Fusion sans doublons
        parentFirst.forEach(c -> { if (!results.contains(c)) results.add(c); });
        parentLast.forEach(c -> { if (!results.contains(c)) results.add(c); });

        return results;
    }


public List<Child> findAll() {
        return childRepository.findAll();
    }

    @Transactional
    public void deleteChild(Integer childId) {

        Child child = childRepository.findById(childId).orElseThrow();

        // 1. Supprimer les conditions médicales
        if (child.getMedicalConditions() != null) {
            childMedicalRepository.deleteAll(child.getMedicalConditions());
        }

        // 2. Supprimer les liens parent-enfant
        if (child.getParentLinks() != null) {
            childParentRepository.deleteAll(child.getParentLinks());
        }

        // 3. Supprimer les inscriptions aux activités
        if (child.getRegistrations() != null) {
            activityRegistrationRepository.deleteAll(child.getRegistrations());
        }

        // 4. Supprimer l'enfant
        childRepository.delete(child);
    }



}






