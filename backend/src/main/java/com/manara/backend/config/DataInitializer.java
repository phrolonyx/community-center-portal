package com.manara.backend.config;

import com.manara.backend.model.*;
import com.manara.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ActivityRepository activityRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private ChildRepository childRepository;
    @Autowired private ParentProfileRepository parentProfileRepository;
    @Autowired private ChildParentRepository childParentRepository;
    @Autowired private TermsVersionRepository termsVersionRepository;
    @Autowired private ActivitySessionRepository activitySessionRepository;
    @Autowired private ActivityRegistrationRepository activityRegistrationRepository;
    @Autowired private ChildMedicalRepository childMedicalRepository;
    @Autowired private ActivityAttendanceRepository activityAttendanceRepository;
    @Autowired private ActivityRegistrationLogRepository activityRegistrationLogRepository;

    @Override
    public void run(String... args) {

        // =====================================================
        // ROLES
        // =====================================================
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("PARENT", "Parent ou tuteur qui peut inscrire ses enfants", true));
            roleRepository.save(new Role("ADMIN", "Administrateur qui peut valider les inscriptions et créer les activités", false));
            roleRepository.save(new Role("ANIMATEUR", "Animateur de séances d'activités", false));
        }

        // =====================================================
        // USERS (admin + SQL users merged)
        // =====================================================
        if (userRepository.count() == 0) {
            Role parentRole   = roleRepository.findByName("PARENT").orElseThrow();
            Role adminRole    = roleRepository.findByName("ADMIN").orElseThrow();
            Role animateurRole = roleRepository.findByName("ANIMATEUR").orElseThrow();

            String hashed = passwordEncoder.encode("password");

            // Admin (from existing DataInitializer)
            User admin = new User();
            admin.setEmail("admin@admin.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("Principal");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setRole(adminRole);
            admin.setEnabled(true);
            admin.setPending(false);
            userRepository.save(admin);

            // SQL users
            userRepository.save(makeUser(parentRole,   "sophie.tremblay@example.com", hashed, "Sophie",  "Tremblay",   "sophie.png", true,  "2024-11-12T09:15:00", "2026-02-18T14:22:00"));
            userRepository.save(makeUser(parentRole,   "marc.gagnon@example.com",     hashed, "Marc",    "Gagnon",     "marc.jpg",   true,  "2025-01-05T16:40:00", "2026-01-22T10:05:00"));
            userRepository.save(makeUser(parentRole,   "julie.roy@example.com",       hashed, "Julie",   "Roy",        "julie.png",  true,  "2024-09-28T08:10:00", "2026-03-01T09:55:00"));
            userRepository.save(makeUser(parentRole,   "karim.benali@example.com",    hashed, "Karim",   "Benali",     null,         true,  "2025-03-14T11:30:00", "2026-02-27T17:20:00"));
            userRepository.save(makeUser(animateurRole,"nadia.hassan@example.com",  hashed, "Nadia",   "Hassan",     "nadia.png",  true,  "2024-12-02T14:05:00", "2026-03-05T13:45:00"));
            userRepository.save(makeUser(animateurRole,"youssef.ali@example.com",   hashed, "Youssef", "Ali",        null,         true,  "2025-02-18T10:50:00", "2026-02-28T15:30:00"));
            userRepository.save(makeUser(parentRole,   "fatima.saadi@example.com",    hashed, "Fatima",  "Saadi",      "fatima.png", true,  "2024-10-21T09:00:00", "2026-01-30T08:40:00"));
            userRepository.save(makeUser(parentRole,   "hassan.elamrani@example.com", hashed, "Hassan",  "El Amrani",  "hassan.png", false, "2025-04-07T13:25:00", "2026-03-03T12:10:00"));
            userRepository.save(makeUser(parentRole,   "mona.khalil@example.com",     hashed, "Mona",    "Khalil",     "mona.png",   true,  "2024-08-19T15:45:00", "2026-02-14T09:55:00"));
            userRepository.save(makeUser(parentRole,   "patrick.lefebvre@example.com",hashed, "Patrick", "Lefebvre",   null,         true,  "2025-05-11T10:10:00", "2026-03-06T11:50:00"));
            userRepository.save(makeUser(parentRole,   "amelie.gauthier@example.com", hashed, "Amélie",  "Gauthier",   "amelie.png", false, "2024-07-30T08:35:00", "2026-02-20T16:05:00"));
        }

        // =====================================================
        // ACTIVITIES
        // =====================================================
        if (activityRepository.count() == 0) {
            activityRepository.saveAll(List.of(
                    makeActivity("Soccer junior",               "Entraînement de soccer pour enfants",  "soccer_junior.png"),
                    makeActivity("Basketball débutant",         "Initiation au basketball",              "basketball_debutant.png"),
                    makeActivity("Atelier de peinture",         "Création artistique et peinture",       "atelier_peinture.png"),
                    makeActivity("Club de sciences",            "Expériences scientifiques amusantes",   "club_sciences.png"),
                    makeActivity("Programmation pour enfants",  "Découverte du code",                    "programmation_enfants.png"),
                    makeActivity("Club d'échecs",               "Stratégie et logique",                  "club_echecs.png"),
                    makeActivity("Cours de musique",            "Introduction aux instruments",           "cours_musique.png"),
                    makeActivity("Cours de danse",              "Danse moderne pour enfants",            "cours_danse.png"),
                    makeActivity("Robotique junior",            "Construction de robots simples",         "robotique_junior.png"),
                    makeActivity("Club de mathématiques",       "Jeux mathématiques et logique",         "club_mathematiques.png")
            ));
        }

        // =====================================================
        // LOCATIONS
        // =====================================================
        if (locationRepository.count() == 0) {
            locationRepository.saveAll(List.of(
                    makeLocation("Centre communautaire Laval", "1200 boul. de la Concorde O, Laval",    30, "Salle principale"),
                    makeLocation("Salle artistique",           "500 rue Sherbrooke O, Montréal",         20, "Ateliers créatifs"),
                    makeLocation("Laboratoire scientifique",   "300 rue Saint-Antoine E, Montréal",      15, "Expériences scientifiques"),
                    makeLocation("Gymnase municipal",          "250 boul. Cartier O, Laval",             25, "Activités sportives"),
                    makeLocation("Salle informatique",         "400 rue Notre-Dame O, Montréal",         12, "Ordinateurs pour programmation")
            ));
        }

        // =====================================================
        // TERMS VERSION
        // =====================================================
        if (termsVersionRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@admin.com").orElseThrow();
            TermsVersion terms = new TermsVersion();
            terms.setVersionLabel("v1.0");
            terms.setContent("Conditions générales de la plateforme");
            terms.setPublishedAt(LocalDateTime.now());
            terms.setAddedBy(admin);
            termsVersionRepository.save(terms);
        }

        // =====================================================
        // PARENT PROFILES
        // =====================================================
        if (parentProfileRepository.count() == 0) {
            TermsVersion terms = termsVersionRepository.findAll().get(0);

            // SQL user_id 2 → sophie.tremblay (index 1 in SQL, but admin is first so offset by 1)
            User sophie  = userRepository.findByEmail("sophie.tremblay@example.com").orElseThrow();
            User marc    = userRepository.findByEmail("marc.gagnon@example.com").orElseThrow();
            User julie   = userRepository.findByEmail("julie.roy@example.com").orElseThrow();
            User karim   = userRepository.findByEmail("karim.benali@example.com").orElseThrow();
            User hassan  = userRepository.findByEmail("hassan.elamrani@example.com").orElseThrow();
            User mona    = userRepository.findByEmail("mona.khalil@example.com").orElseThrow();
            User patrick = userRepository.findByEmail("patrick.lefebvre@example.com").orElseThrow();
            User amelie  = userRepository.findByEmail("amelie.gauthier@example.com").orElseThrow();
            User fatima  = userRepository.findByEmail("fatima.saadi@example.com").orElseThrow();

            // pp1 → sophie (user_id=2 in SQL)
            parentProfileRepository.save(makeParentProfile(sophie,  terms, "2024-11-15T09:40:00", "2025-02-10T14:20:00", "5145551111", "H7N2K3", "Laval",    "Québec", "Canada", "45 rue des Érables",  "Jean Tremblay",   "Père",      "5145559001"));
            // pp2 → marc
            parentProfileRepository.save(makeParentProfile(marc,    terms, "2024-10-02T13:25:00", "2025-01-18T10:55:00", "5145552222", "H2X1Y4", "Montréal", "Québec", "Canada", "120 rue Saint-Denis", null,              null,        null));
            // pp3 → julie
            parentProfileRepository.save(makeParentProfile(julie,   terms, "2025-01-12T08:50:00", "2025-03-05T16:40:00", "4385553333", "H7V3A1", "Laval",    "Québec", "Canada", "78 rue du Parc",      "Sophie Gagnon",   "Conjointe", "4385559003"));
            // pp4 → karim
            parentProfileRepository.save(makeParentProfile(karim,   terms, "2024-12-20T11:10:00", "2025-02-27T09:35:00", "5145554444", "H3B2Y5", "Montréal", "Québec", "Canada", "2100 rue Peel",       null,              null,        null));
            // pp5 → hassan
            parentProfileRepository.save(makeParentProfile(hassan,  terms, "2025-03-03T15:45:00", "2025-05-12T12:10:00", "4385555555", "H7M1A4", "Laval",    "Québec", "Canada", "12 rue des Lilas",    "Nadia Benali",    "Soeur",     "4385559005"));
            // pp6 → mona
            parentProfileRepository.save(makeParentProfile(mona,    terms, "2024-09-28T10:05:00", "2025-01-09T17:20:00", "5145556666", "H2L3R2", "Montréal", "Québec", "Canada", "90 rue Ontario E",    null,              null,        null));
            // pp7 → patrick
            parentProfileRepository.save(makeParentProfile(patrick, terms, "2025-02-14T14:30:00", "2025-04-01T09:50:00", "4385557777", "H7P4B2", "Laval",    "Québec", "Canada", "34 rue des Pins",     "Patrick Lefebvre","Père",      "4385559007"));
            // pp8 → amelie
            parentProfileRepository.save(makeParentProfile(amelie,  terms, "2024-11-07T16:55:00", "2025-02-22T11:40:00", "5145558888", "H2Y2T1", "Montréal", "Québec", "Canada", "15 rue McGill",       "Amélie Gauthier", "Conjointe", "5145559008"));
            // pp9 → fatima
            parentProfileRepository.save(makeParentProfile(fatima,  terms, "2025-01-25T09:15:00", "2025-03-18T13:05:00", "4385559999", "H7K3L9", "Laval",    "Québec", "Canada", "67 rue des Cèdres",   null,              null,        null));
        }

        // =====================================================
        // CHILDREN
        // =====================================================
        if (childRepository.count() == 0) {
            childRepository.saveAll(List.of(
                    makeChild("Adam",    "Tremblay", "2017-02-10T00:00:00", "2025-11-22T14:10:00"),
                    makeChild("Lina",    "Tremblay", "2016-06-15T00:00:00", "2024-09-05T09:45:00"),
                    makeChild("Omar",    "Gagnon",   "2018-03-12T00:00:00", "2025-03-18T16:20:00"),
                    makeChild("Noor",    "Gagnon",   "2015-07-20T00:00:00", "2024-12-01T11:05:00"),
                    makeChild("Ali",     "Roy",      "2017-11-05T00:00:00", "2025-07-14T08:55:00"),
                    makeChild("Sara",    "Roy",      "2016-10-14T00:00:00", "2025-02-27T13:40:00"),
                    makeChild("Huda",    "Benali",   "2018-04-01T00:00:00", "2024-10-29T10:30:00"),
                    makeChild("Yassine", "Benali",   "2017-09-19T00:00:00", "2025-05-09T15:50:00"),
                    makeChild("Mariam",  "Saadi",    "2016-08-22T00:00:00", "2024-08-17T12:15:00"),
                    makeChild("Tariq",   "El Amrani","2015-05-18T00:00:00", "2025-01-23T17:25:00")
            ));
        }

        // =====================================================
        // CHILD-PARENT LINKS
        // =====================================================
        if (childParentRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@admin.com").orElseThrow();
            List<Child> children       = childRepository.findAll();
            List<ParentProfile> parents = parentProfileRepository.findAll();

            // child index is 0-based, parent profile index is 0-based
            // SQL: (child_id, parent_profile_id, relationship)
            childParentRepository.saveAll(List.of(
                    makeChildParent(children.get(0), parents.get(0), "Mère",  admin, "2024-06-20T09:15:00"),
                    makeChildParent(children.get(1), parents.get(0), "Mère",  admin, "2024-07-05T10:40:00"),
                    makeChildParent(children.get(2), parents.get(1), "Père",  admin, "2024-09-12T14:25:00"),
                    makeChildParent(children.get(3), parents.get(1), "Père",  admin, "2024-10-03T16:10:00"),
                    makeChildParent(children.get(4), parents.get(2), "Mère",  admin, "2024-11-18T11:55:00"),
                    makeChildParent(children.get(5), parents.get(2), "Mère",  admin, "2025-01-22T13:35:00"),
                    makeChildParent(children.get(6), parents.get(3), "Père",  admin, "2024-08-29T15:20:00"),
                    makeChildParent(children.get(7), parents.get(3), "Père",  admin, "2024-09-30T09:50:00"),
                    makeChildParent(children.get(8), parents.get(4), "Mère",  admin, "2024-12-14T10:05:00"),
                    makeChildParent(children.get(9), parents.get(5), "Père",  admin, "2025-02-08T14:45:00")
            ));
        }

        // =====================================================
        // ACTIVITY SESSIONS
        // =====================================================
        if (activitySessionRepository.count() == 0) {
            List<Activity> acts  = activityRepository.findAll();
            List<Location> locs  = locationRepository.findAll();
            User nadia   = userRepository.findByEmail("nadia.hassan@example.com").orElseThrow();
            User youssef = userRepository.findByEmail("youssef.ali@example.com").orElseThrow();

            // host_name_user_id 6 = nadia, 7 = youssef (SQL user order)
            activitySessionRepository.saveAll(List.of(
                    makeSession(acts.get(0), 15, nadia,   locs.get(3), "2026-04-01T10:00:00", "2026-04-01T11:30:00",4,8),
                    makeSession(acts.get(1), 15, nadia,   locs.get(3), "2026-04-02T10:00:00", "2026-04-02T11:30:00", 5, 9),
                    makeSession(acts.get(2), 12, youssef, locs.get(1), "2026-04-03T14:00:00", "2026-04-03T15:30:00", 4, 10),
                    makeSession(acts.get(3), 10, youssef, locs.get(2), "2026-04-04T15:00:00", "2026-04-04T16:30:00", 6, 10),
                    makeSession(acts.get(4), 10, nadia,   locs.get(4), "2026-04-05T16:00:00", "2026-04-05T17:30:00", 7, 11),
                    makeSession(acts.get(5), 10, youssef, locs.get(0), "2026-04-06T10:00:00", "2026-04-06T11:30:00", 6, 12),
                    makeSession(acts.get(6), 12, nadia,   locs.get(0), "2026-04-07T10:00:00", "2026-04-07T11:30:00", 5, 10),
                    makeSession(acts.get(7), 12, youssef, locs.get(0), "2026-04-08T10:00:00", "2026-04-08T11:30:00", 4, 9),
                    makeSession(acts.get(8), 10, nadia,   locs.get(4), "2026-04-09T14:00:00", "2026-04-09T15:30:00", 7, 11),
                    makeSession(acts.get(9), 10, youssef, locs.get(2), "2026-04-10T14:00:00", "2026-04-10T15:30:00", 6, 10)
            ));
        }

        // =====================================================
        // ACTIVITY REGISTRATIONS
        // =====================================================
        if (activityRegistrationRepository.count() == 0) {
            List<ActivitySession>  sessions = activitySessionRepository.findAll();
            List<Child>            children = childRepository.findAll();
            List<ParentProfile>    parents  = parentProfileRepository.findAll();

            activityRegistrationRepository.saveAll(List.of(
                    makeRegistration(sessions.get(0), children.get(0), parents.get(0), "CONFIRMÉ",  "2025-11-12T09:20:00", "2025-11-13T10:05:00", null),
                    makeRegistration(sessions.get(1), children.get(1), parents.get(0), "EN_ATTENTE", "2024-10-03T14:45:00", null,                  null),
                    makeRegistration(sessions.get(2), children.get(2), parents.get(1), "CONFIRMÉ",  "2025-03-18T11:10:00", "2025-03-19T09:30:00", null),
                    makeRegistration(sessions.get(3), children.get(3), parents.get(1), "ANNULÉ",    "2024-12-27T16:05:00", "2024-12-28T08:50:00", "2024-12-29T08:50:00"),
                    makeRegistration(sessions.get(4), children.get(4), parents.get(2), "CONFIRMÉ",  "2025-07-02T08:55:00", "2025-07-03T14:10:00", null),
                    makeRegistration(sessions.get(5), children.get(5), parents.get(2), "CONFIRMÉ",  "2025-02-14T13:30:00", "2025-02-15T10:45:00", null),
                    makeRegistration(sessions.get(6), children.get(6), parents.get(3), "EN_ATTENTE", "2024-09-21T10:40:00", null,                  null),
                    makeRegistration(sessions.get(7), children.get(7), parents.get(3), "CONFIRMÉ",  "2025-05-29T15:15:00", "2025-05-30T09:20:00", null),
                    makeRegistration(sessions.get(8), children.get(8), parents.get(4), "CONFIRMÉ",  "2024-11-08T17:50:00", "2024-11-09T11:00:00", null),
                    makeRegistration(sessions.get(9), children.get(9), parents.get(5), "CONFIRMÉ",  "2025-01-19T12:25:00", "2025-01-20T08:35:00", null)
            ));
        }

        // =====================================================
        // CHILD MEDICAL
        // =====================================================
        if (childMedicalRepository.count() == 0) {
            List<Child> children = childRepository.findAll();
            childMedicalRepository.saveAll(List.of(
                    makeMedical(children.get(0), "Allergie aux arachides",  "Porte un EpiPen",                    "ÉLEVÉ",  "2024-06-15T10:25:00"),
                    makeMedical(children.get(1), "Asthme",                  "Utilise un inhalateur si nécessaire", "MOYEN",  "2025-02-11T14:40:00"),
                    makeMedical(children.get(2), "Aucune condition",         "En bonne santé",                     "FAIBLE", "2024-09-03T09:15:00"),
                    makeMedical(children.get(3), "Intolérance au lactose",   "Éviter les produits laitiers",       "FAIBLE", "2025-01-22T16:55:00"),
                    makeMedical(children.get(4), "Allergie aux noix",        "Surveiller les collations",          "ÉLEVÉ",  "2024-11-19T11:30:00"),
                    makeMedical(children.get(5), "Asthme léger",             "Inhalateur disponible",              "MOYEN",  "2025-03-14T08:45:00"),
                    makeMedical(children.get(6), "Aucune condition",         "Aucun problème",                     "FAIBLE", "2024-07-28T13:20:00"),
                    makeMedical(children.get(7), "Allergie saisonnière",     "Réaction au pollen",                 "FAIBLE", "2025-05-09T15:10:00"),
                    makeMedical(children.get(8), "Aucune condition",         "En bonne santé",                     "FAIBLE", "2024-10-02T10:05:00"),
                    makeMedical(children.get(9), "Allergie aux arachides",   "EpiPen dans le sac",                 "ÉLEVÉ",  "2025-04-17T17:40:00")
            ));
        }

        // =====================================================
        // ACTIVITY ATTENDANCE
        // =====================================================
        if (activityAttendanceRepository.count() == 0) {
            List<ActivityRegistration> regs = activityRegistrationRepository.findAll();
            User nadia   = userRepository.findByEmail("nadia.hassan@example.com").orElseThrow();
            User youssef = userRepository.findByEmail("youssef.ali@example.com").orElseThrow();

            activityAttendanceRepository.saveAll(List.of(
                    makeAttendance(nadia,   regs.get(0), "PRÉSENT", "2026-04-01T12:05:00", "Arrivé à l'heure"),
                    makeAttendance(nadia,   regs.get(1), "PRÉSENT", "2026-04-02T12:10:00", "Participation active"),
                    makeAttendance(youssef, regs.get(2), "PRÉSENT", "2026-04-03T16:20:00", "Très motivé"),
                    makeAttendance(youssef, regs.get(3), "ABSENT",  "2026-04-04T17:15:00", "Absence signalée par les parents"),
                    makeAttendance(nadia,   regs.get(4), "PRÉSENT", "2026-04-05T18:25:00", "Bonne participation"),
                    makeAttendance(nadia,   regs.get(5), "PRÉSENT", "2026-04-06T12:00:00", "Très concentré"),
                    makeAttendance(youssef, regs.get(6), "PRÉSENT", "2026-04-07T12:15:00", "En retard de 10 minutes"),
                    makeAttendance(youssef, regs.get(7), "PRÉSENT", "2026-04-08T12:40:00", "Participation correcte"),
                    makeAttendance(nadia,   regs.get(8), "ABSENT",  "2026-04-09T16:10:00", "Absent pour voyage"),
                    makeAttendance(nadia,   regs.get(9), "PRÉSENT", "2026-04-10T16:35:00", "Très enthousiaste")
            ));
        }

        // =====================================================
        // ACTIVITY REGISTRATION LOGS
        // =====================================================
        if (activityRegistrationLogRepository.count() == 0) {
            User admin = userRepository.findByEmail("admin@admin.com").orElseThrow();
            List<ActivityRegistration> regs = activityRegistrationRepository.findAll();

            activityRegistrationLogRepository.saveAll(List.of(
                    makeLog(admin, regs.get(0), "CONFIRMÉ",   "EN_ATTENTE", "2025-11-12T10:10:00", "Validation par administrateur"),
                    makeLog(admin, regs.get(1), "EN_ATTENTE", null,         "2024-10-03T15:00:00", "Inscription initiale"),
                    makeLog(admin, regs.get(2), "CONFIRMÉ",   "EN_ATTENTE", "2025-03-18T12:05:00", "Place disponible"),
                    makeLog(admin, regs.get(3), "ANNULÉ",     "CONFIRMÉ",   "2024-12-28T09:20:00", "Annulation demandée par le parent"),
                    makeLog(admin, regs.get(4), "CONFIRMÉ",   "EN_ATTENTE", "2025-07-02T10:30:00", "Inscription validée"),
                    makeLog(admin, regs.get(5), "CONFIRMÉ",   "EN_ATTENTE", "2025-02-14T14:10:00", "Confirmation automatique"),
                    makeLog(admin, regs.get(6), "EN_ATTENTE", null,         "2024-09-21T11:05:00", "Liste d'attente"),
                    makeLog(admin, regs.get(7), "CONFIRMÉ",   "EN_ATTENTE", "2025-05-29T16:00:00", "Validation du système"),
                    makeLog(admin, regs.get(8), "CONFIRMÉ",   "EN_ATTENTE", "2024-11-08T18:20:00", "Place confirmée"),
                    makeLog(admin, regs.get(9), "CONFIRMÉ",   "EN_ATTENTE", "2025-01-19T13:10:00", "Inscription validée")
            ));
        }
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private User makeUser(Role role, String email, String passwordHash, String firstName, String lastName,
                          String imagePath, boolean enabled, String createdAt, String lastLogin) {
        User u = new User();
        u.setRole(role);
        u.setEmail(email);
        u.setPasswordHash(passwordHash);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setUserImagePath(imagePath);
        u.setEnabled(enabled);
        u.setCreatedAt(LocalDateTime.parse(createdAt));
        u.setLastLogin(LocalDateTime.parse(lastLogin));
        u.setPending(false);
        return u;
    }

    private Activity makeActivity(String name, String description, String imagePath) {
        Activity a = new Activity();
        a.setName(name);
        a.setDescription(description);
        a.setActivityImagePath("/images/" + imagePath);
        return a;
    }

    private Location makeLocation(String name, String address, int capacity, String description) {
        Location l = new Location();
        l.setName(name);
        l.setAddress(address);
        l.setCapacity(capacity);
        l.setDescription(description);
        return l;
    }

    private Child makeChild(String firstName, String lastName, String dob, String updatedAt) {
        Child c = new Child();
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setDateOfBirth(LocalDateTime.parse(dob));
        c.setUpdatedAt(LocalDateTime.parse(updatedAt));
        return c;
    }

    private ParentProfile makeParentProfile(User user, TermsVersion terms, String termsAcceptAt, String updatedAt,
                                            String phone, String postalCode, String city, String province,
                                            String country, String address,
                                            String emergencyName, String emergencyRelationship, String emergencyPhone) {
        ParentProfile pp = new ParentProfile();
        pp.setUser(user);
        pp.setTermsVersion(terms);
        pp.setTermsAcceptAt(LocalDateTime.parse(termsAcceptAt));
        pp.setUpdatedAt(LocalDateTime.parse(updatedAt));
        pp.setPhoneNumber(phone);
        pp.setPostalCode(postalCode);
        pp.setCity(city);
        pp.setProvince(province);
        pp.setCountry(country);
        pp.setAddressLine1(address);
        pp.setEmergencyName(emergencyName);
        pp.setEmergencyRelationship(emergencyRelationship);
        pp.setEmergencyPhone(emergencyPhone);
        return pp;
    }

    private ChildParent makeChildParent(Child child, ParentProfile parentProfile, String relationship,
                                        User validatedBy, String createdAt) {
        ChildParent cp = new ChildParent();
        cp.setChild(child);
        cp.setParentProfile(parentProfile);
        cp.setRelationship(relationship);
        cp.setIsPrimary(true);
        cp.setValidatedBy(validatedBy);
        cp.setCreatedAt(LocalDateTime.parse(createdAt));
        return cp;
    }

    private ActivitySession makeSession(Activity activity, int capacity, User host, Location location,
                                        String start, String end, int minAge, int maxAge) {
        ActivitySession s = new ActivitySession();
        s.setActivity(activity);
        s.setCapacity(capacity);
        s.setHost(host);
        s.setLocation(location);
        s.setDateTimeStart(LocalDateTime.parse(start));
        s.setDateTimeEnd(LocalDateTime.parse(end));
        s.setMinAge(minAge);
        s.setMaxAge(maxAge);
        return s;
    }

    private ActivityRegistration makeRegistration(ActivitySession session, Child child, ParentProfile parent,
                                                  String status, String createdAt,
                                                  String confirmedAt, String cancelledAt) {
        ActivityRegistration r = new ActivityRegistration();
        r.setActivitySession(session);
        r.setChild(child);
        r.setParentProfile(parent);
        r.setStatus(status);
        r.setCreatedAt(LocalDateTime.parse(createdAt));
        r.setConfirmedAt(confirmedAt != null ? LocalDateTime.parse(confirmedAt) : null);
        r.setCancelledAt(cancelledAt != null ? LocalDateTime.parse(cancelledAt) : null);
        return r;
    }

    private ChildMedical makeMedical(Child child, String condition, String notes, String severity, String updatedAt) {
        ChildMedical m = new ChildMedical();
        m.setChild(child);
        m.setCondition(condition);
        m.setNotes(notes);
        m.setSeverity(severity);
        m.setUpdatedAt(LocalDateTime.parse(updatedAt));
        return m;
    }

    private ActivityAttendance makeAttendance(User markedBy, ActivityRegistration registration,
                                              String status, String markedAt, String note) {
        ActivityAttendance a = new ActivityAttendance();
        a.setMarkedBy(markedBy);
        a.setRegistration(registration);
        a.setStatus(status);
        a.setMarkedAt(LocalDateTime.parse(markedAt));
        a.setNote(note);
        return a;
    }

    private ActivityRegistrationLog makeLog(User changedBy, ActivityRegistration registration,
                                            String newStatus, String oldStatus,
                                            String changedAt, String note) {
        ActivityRegistrationLog log = new ActivityRegistrationLog();
        log.setChangedBy(changedBy);
        log.setRegistration(registration);
        log.setNewStatus(newStatus);
        log.setOldStatus(oldStatus);
        log.setChangedAt(LocalDateTime.parse(changedAt));
        log.setNote(note);
        return log;
    }
}