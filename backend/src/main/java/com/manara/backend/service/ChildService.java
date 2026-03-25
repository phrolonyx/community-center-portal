package com.manara.backend.service;

import com.manara.backend.model.Child;
import com.manara.backend.model.ChildMedical;
import com.manara.backend.model.ChildParent;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.repository.ChildMedicalRepository;
import com.manara.backend.repository.ChildParentRepository;
import com.manara.backend.repository.ChildRepository;
import com.manara.backend.repository.ParentProfileRepository;
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

        // Si la recherche est vide = on retourne tous les enfants
        if (keyword == null || keyword.trim().isEmpty()) {
            return childRepository.findAll();
        }

        keyword = keyword.trim();

        // recherche par prenom ou nom
        return childRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                keyword, keyword
        );
    }

    public List<Child> findAll() {
        return childRepository.findAll();
    }

    @Transactional
    public void deleteChild(Integer childId) {
        childMedicalRepository.deleteAll(
                childRepository.findById(childId).orElseThrow().getMedicalConditions()
        );
        childParentRepository.deleteAll(
                childRepository.findById(childId).orElseThrow().getParentLinks()
        );
        childRepository.deleteById(childId);
    }



}


