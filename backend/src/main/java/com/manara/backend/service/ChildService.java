package com.manara.backend.service;

import com.manara.backend.dto.ChildRequest;
import com.manara.backend.model.*;
import com.manara.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
        createChild(child, parentProfileId, null);
    }

    @Transactional
    public void createChild(Child child, Integer parentProfileId, String relationship) {
        ParentProfile parentProfile = parentProfileRepository.findById(parentProfileId).orElseThrow();
        ChildParent childParent = new ChildParent(child, parentProfile);
        if (relationship != null && !relationship.isBlank()) {
            childParent.setRelationship(relationship);
        }
        childRepository.save(child);
        childParentRepository.save(childParent);
    }

    @Transactional
    public Child createChild(ChildRequest childRequest) {
        Child child = childRequest.toChild();
        ParentProfile parentProfile = parentProfileRepository.findById(childRequest.getParentId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent Profile Non trouve"));
        ChildParent childParent = new ChildParent(child, parentProfile);
        childParent.setRelationship(childRequest.getRelationship());

        childRepository.save(child);
        childParentRepository.save(childParent);

        if (childRequest.getMedical() != null && !childRequest.getMedical().isBlank()) {
            ChildMedical medical = new ChildMedical();
            medical.setChild(child);
            medical.setCondition(childRequest.getMedical());
            medical.setUpdatedAt(LocalDateTime.now());
            childMedicalRepository.save(medical);
        }

        return child;
    }

    public Child findById(Integer id) {
        return childRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "child non trouvé"));
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

    // Met a jour la relation Parent-Enfant
    @Transactional
    public void updateRelationship(Integer childId, Integer parentProfileId, String relationship) {
        if (relationship == null || relationship.isBlank()) return;
        List<ChildParent> liens = childParentRepository.findChildParentsByParentProfile_Id(parentProfileId);
        for (ChildParent cp : liens) {
            if (cp.getChild().getId().equals(childId)) {
                cp.setRelationship(relationship);
                childParentRepository.save(cp);
                return;
            }
        }
    }

    @Transactional
    public Child updateChild(Integer id, ChildRequest childRequest) {
        Child child = findById(id);

        child.setFirstName(childRequest.getFirstName());
        child.setLastName(childRequest.getLastName());
        child.setDateOfBirth(childRequest.getDateOfBirth());
        child.setUpdatedAt(LocalDateTime.now());
        childRepository.save(child);

        if (childRequest.getMedical() != null && !childRequest.getMedical().isBlank()) {
            ChildMedical medical = childMedicalRepository.findChildMedicalByChild(child);

            if (medical == null) {
                medical = new ChildMedical();
                medical.setChild(child);
            }

            medical.setCondition(childRequest.getMedical());
            medical.setUpdatedAt(LocalDateTime.now());
            childMedicalRepository.save(medical);
        }

        return child;
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

        Child child = findById(childId);

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






