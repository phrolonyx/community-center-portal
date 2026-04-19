package com.manara.backend.service;

import com.manara.backend.dto.RegisterRequest;
import com.manara.backend.model.ParentProfile;
import com.manara.backend.model.User;
import com.manara.backend.repository.ParentProfileRepository;
import com.manara.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * Service pour gerer les profils parent.
 * Permet de creer, chercher et mettre a jour un profil.
 */
@Service
public class ParentService {

    @Autowired
    private ParentProfileRepository parentProfileRepository;

    @Autowired
    private UserRepository userRepository;

    // Creer un profil parent vide (utilise par l'admin)
    @Transactional
    public void createProfile(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        ParentProfile parentProfile = new ParentProfile(user);
        parentProfileRepository.save(parentProfile);
    }

    // Chercher les profils parent par userId
    public List<ParentProfile> findByUserId(Integer userId) {
        return parentProfileRepository.findAllByUserId(userId);
    }

    // Mettre a jour un profil parent
    @Transactional
    public void updateProfile(ParentProfile profile) {
        parentProfileRepository.save(profile);
    }

    // Creer un profil parent a partir du formulaire d'inscription
    @Transactional
    public void createProfileFromInscription(User user, RegisterRequest dto) {
        ParentProfile profile = new ParentProfile(user);
        profile.setPhoneNumber(dto.getTelephone());
        profile.setAddressLine1(dto.getAdresse());
        profile.setCity(dto.getVille());
        profile.setProvince(dto.getProvince());
        profile.setCountry("Canada");
        profile.setPostalCode(dto.getPostalCode());
        profile.setEmergencyName(dto.getUrgenceNom());
        profile.setEmergencyPhone(dto.getUrgenceTel());
        profile.setEmergencyRelationship(dto.getUrgenceRelation());
        parentProfileRepository.save(profile);
    }

    // Mettre a jour les infos de l'utilisateur (prenom, nom, email)
    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
