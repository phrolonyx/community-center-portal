USE manara_db;

-- =====================================================
-- ROLES
-- =====================================================
-- INSERT INTO role (is_default, name, description) VALUES
-- (b'1','Parent','Rôle par défaut pour les parents'),
-- (b'0','Administrateur','Gestion complète de la plateforme'),
-- (b'0','Animateur','Responsable des activités');

-- =====================================================
-- UTILISATEURS
-- =====================================================
INSERT INTO user (enabled, role_id, created_at, last_login, first_name, last_name, email, password_hash, user_image_path)
VALUES
(b'1',1,'2024-11-12 09:15:00','2026-02-18 14:22:00','Sophie','Tremblay','sophie.tremblay@mail.ca','hash2', 'sophie.png'),
(b'1',1,'2025-01-05 16:40:00','2026-01-22 10:05:00','Marc','Gagnon','marc.gagnon@mail.ca','hash3', 'marc.jpg'),
(b'1',1,'2024-09-28 08:10:00','2026-03-01 09:55:00','Julie','Roy','julie.roy@mail.ca','hash4', 'julie.png'),
(b'1',1,'2025-03-14 11:30:00','2026-02-27 17:20:00','Karim','Benali','karim.benali@mail.ca','hash5', null),
(b'1',3,'2024-12-02 14:05:00','2026-03-05 13:45:00','Nadia','Hassan','nadia.hassan@manara.ca','hash6', 'nadia.png'),
(b'1',3,'2025-02-18 10:50:00','2026-02-28 15:30:00','Youssef','Ali','youssef.ali@manara.ca','hash7', null),
(b'1',1,'2024-10-21 09:00:00','2026-01-30 08:40:00','Fatima','Saadi','fatima.saadi@mail.ca','hash8', 'fatima.png'),
(b'0',1,'2025-04-07 13:25:00','2026-03-03 12:10:00','Hassan','El Amrani','hassan.elamrani@mail.ca','hash9', 'hassan.png'),
(b'1',1,'2024-08-19 15:45:00','2026-02-14 09:55:00','Mona','Khalil','mona.khalil@mail.ca','hash10', 'mona.png'),
(b'1',1,'2025-05-11 10:10:00','2026-03-06 11:50:00','Patrick','Lefebvre','patrick.lefebvre@mail.ca','hash11', null),
(b'0',1,'2024-07-30 08:35:00','2026-02-20 16:05:00','Amélie','Gauthier','amelie.gauthier@mail.ca','hash12', 'amelie.png');


-- =====================================================
-- ACTIVITÉS
-- =====================================================
INSERT INTO activity (name, description, activity_image_path) VALUES
('Soccer junior','Entraînement de soccer pour enfants','soccer_junior.png'),
('Basketball débutant','Initiation au basketball','basketball_debutant.png'),
('Atelier de peinture','Création artistique et peinture','atelier_peinture.png'),
('Club de sciences','Expériences scientifiques amusantes','club_sciences.png'),
('Programmation pour enfants','Découverte du code','programmation_enfants.png'),
('Club d’échecs','Stratégie et logique','club_echecs.png'),
('Cours de musique','Introduction aux instruments','cours_musique.png'),
('Cours de danse','Danse moderne pour enfants','cours_danse.png'),
('Robotique junior','Construction de robots simples','robotique_junior.png'),
('Club de mathématiques','Jeux mathématiques et logique','club_mathematiques.png');


-- =====================================================
-- LIEUX
-- =====================================================
INSERT INTO location (capacity,name,address,description) VALUES
(30,'Centre communautaire Laval','1200 boul. de la Concorde O, Laval','Salle principale'),
(20,'Salle artistique','500 rue Sherbrooke O, Montréal','Ateliers créatifs'),
(15,'Laboratoire scientifique','300 rue Saint-Antoine E, Montréal','Expériences scientifiques'),
(25,'Gymnase municipal','250 boul. Cartier O, Laval','Activités sportives'),
(12,'Salle informatique','400 rue Notre-Dame O, Montréal','Ordinateurs pour programmation');

-- =====================================================
-- CONDITIONS (TERMS)
-- =====================================================
INSERT INTO terms_version (added_by_id,published_at,version_label,content)
VALUES
(1,NOW(),'v1.0','Conditions générales de la plateforme Manara');

-- =====================================================
-- PROFILS PARENTS
-- =====================================================
INSERT INTO parent_profile
(terms_version_id, user_id, terms_accept_at, updated_at, phone_number, postal_code, city, country, province, address_line1,
 emergency_name, emergency_relationship, emergency_phone)
VALUES
(1,2,'2024-11-15 09:40:00','2025-02-10 14:20:00','5145551111','H7N2K3','Laval','Canada','Québec','45 rue des Érables', 'Jean Tremblay','Père','5145559001'),

(1,3,'2024-10-02 13:25:00','2025-01-18 10:55:00','5145552222','H2X1Y4','Montréal','Canada','Québec','120 rue Saint-Denis', NULL, NULL, NULL),

(1,4,'2025-01-12 08:50:00','2025-03-05 16:40:00','4385553333','H7V3A1','Laval','Canada','Québec','78 rue du Parc', 'Sophie Gagnon','Conjointe','4385559003'),

(1,5,'2024-12-20 11:10:00','2025-02-27 09:35:00','5145554444','H3B2Y5','Montréal','Canada','Québec','2100 rue Peel', NULL, NULL, NULL),

(1,8,'2025-03-03 15:45:00','2025-05-12 12:10:00','4385555555','H7M1A4','Laval','Canada','Québec','12 rue des Lilas', 'Nadia Benali', 'Soeur', '4385559005'),

(1,9,'2024-09-28 10:05:00','2025-01-09 17:20:00','5145556666','H2L3R2','Montréal','Canada','Québec','90 rue Ontario E', NULL, NULL, NULL),

(1,10,'2025-02-14 14:30:00','2025-04-01 09:50:00','4385557777','H7P4B2','Laval','Canada','Québec','34 rue des Pins', 'Patrick Lefebvre','Père','4385559007'),

(1,11,'2024-11-07 16:55:00','2025-02-22 11:40:00','5145558888','H2Y2T1','Montréal','Canada','Québec','15 rue McGill', 'Amélie Gauthier','Conjointe','5145559008'),

(1,12,'2025-01-25 09:15:00','2025-03-18 13:05:00','4385559999','H7K3L9','Laval','Canada','Québec','67 rue des Cèdres', NULL,NULL,NULL);



-- =====================================================
-- ENFANTS
-- =====================================================
INSERT INTO child (date_of_birth, updated_at, first_name, last_name)
VALUES
('2017-02-10','2025-11-22 14:10:00','Adam','Tremblay'),
('2016-06-15','2024-09-05 09:45:00','Lina','Tremblay'),
('2018-03-12','2025-03-18 16:20:00','Omar','Gagnon'),
('2015-07-20','2024-12-01 11:05:00','Noor','Gagnon'),
('2017-11-05','2025-07-14 08:55:00','Ali','Roy'),
('2016-10-14','2025-02-27 13:40:00','Sara','Roy'),
('2018-04-01','2024-10-29 10:30:00','Huda','Benali'),
('2017-09-19','2025-05-09 15:50:00','Yassine','Benali'),
('2016-08-22','2024-08-17 12:15:00','Mariam','Saadi'),
('2015-05-18','2025-01-23 17:25:00','Tariq','El Amrani');


-- =====================================================
-- RELATION PARENT-ENFANT
-- =====================================================
INSERT INTO child_parent
(child_id, parent_profile_id, is_primary, validated_by_user_id, created_at, relationship)
VALUES
(1,1,b'1',1,'2024-06-20 09:15:00','Mère'),
(2,1,b'1',1,'2024-07-05 10:40:00','Mère'),
(3,2,b'1',1,'2024-09-12 14:25:00','Père'),
(4,2,b'1',1,'2024-10-03 16:10:00','Père'),
(5,3,b'1',1,'2024-11-18 11:55:00','Mère'),
(6,3,b'1',1,'2025-01-22 13:35:00','Mère'),
(7,4,b'1',1,'2024-08-29 15:20:00','Père'),
(8,4,b'1',1,'2024-09-30 09:50:00','Père'),
(9,5,b'1',1,'2024-12-14 10:05:00','Mère'),
(10,6,b'1',1,'2025-02-08 14:45:00','Père');


-- =====================================================
-- SESSIONS D'ACTIVITÉ
-- =====================================================
INSERT INTO activity_session
(activity_id,capacity,host_name_user_id,location_id,date_time_start,date_time_end)
VALUES
(1,15,6,4,'2026-04-01 10:00:00','2026-04-01 11:30:00'),
(2,15,6,4,'2026-04-02 10:00:00','2026-04-02 11:30:00'),
(3,12,7,2,'2026-04-03 14:00:00','2026-04-03 15:30:00'),
(4,10,7,3,'2026-04-04 15:00:00','2026-04-04 16:30:00'),
(5,10,6,5,'2026-04-05 16:00:00','2026-04-05 17:30:00'),
(6,10,7,1,'2026-04-06 10:00:00','2026-04-06 11:30:00'),
(7,12,6,1,'2026-04-07 10:00:00','2026-04-07 11:30:00'),
(8,12,7,1,'2026-04-08 10:00:00','2026-04-08 11:30:00'),
(9,10,6,5,'2026-04-09 14:00:00','2026-04-09 15:30:00'),
(10,10,7,3,'2026-04-10 14:00:00','2026-04-10 15:30:00');

-- =====================================================
-- INSCRIPTIONS
-- =====================================================
INSERT INTO activity_registration
(activity_session_id, child_id, parent_profile_id, created_at, confirmed_at, cancelled_at, status)
VALUES
(1,1,1,'2025-11-12 09:20:00','2025-11-13 10:05:00',NULL,'CONFIRMÉ'),
(2,2,1,'2024-10-03 14:45:00',NULL,NULL,'EN_ATTENTE'),
(3,3,2,'2025-03-18 11:10:00','2025-03-19 09:30:00',NULL,'CONFIRMÉ'),
(4,4,2,'2024-12-27 16:05:00','2024-12-28 08:50:00','2024-12-29 08:50:00','ANNULÉ'),
(5,5,3,'2025-07-02 08:55:00','2025-07-03 14:10:00',NULL,'CONFIRMÉ'),
(6,6,3,'2025-02-14 13:30:00','2025-02-15 10:45:00',NULL,'CONFIRMÉ'),
(7,7,4,'2024-09-21 10:40:00',NULL,NULL,'EN_ATTENTE'),
(8,8,4,'2025-05-29 15:15:00','2025-05-30 09:20:00',NULL,'CONFIRMÉ'),
(9,9,5,'2024-11-08 17:50:00','2024-11-09 11:00:00',NULL,'CONFIRMÉ'),
(10,10,6,'2025-01-19 12:25:00','2025-01-20 08:35:00',NULL,'CONFIRMÉ');


-- =====================================================
-- DONNÉES MÉDICALES DES ENFANTS
-- =====================================================
INSERT INTO child_medical
(child_id, updated_at, medical_condition, notes, severity)
VALUES
(1,'2024-06-15 10:25:00','Allergie aux arachides','Porte un EpiPen','ÉLEVÉ'),
(2,'2025-02-11 14:40:00','Asthme','Utilise un inhalateur si nécessaire','MOYEN'),
(3,'2024-09-03 09:15:00','Aucune condition','En bonne santé','FAIBLE'),
(4,'2025-01-22 16:55:00','Intolérance au lactose','Éviter les produits laitiers','FAIBLE'),
(5,'2024-11-19 11:30:00','Allergie aux noix','Surveiller les collations','ÉLEVÉ'),
(6,'2025-03-14 08:45:00','Asthme léger','Inhalateur disponible','MOYEN'),
(7,'2024-07-28 13:20:00','Aucune condition','Aucun problème','FAIBLE'),
(8,'2025-05-09 15:10:00','Allergie saisonnière','Réaction au pollen','FAIBLE'),
(9,'2024-10-02 10:05:00','Aucune condition','En bonne santé','FAIBLE'),
(10,'2025-04-17 17:40:00','Allergie aux arachides','EpiPen dans le sac','ÉLEVÉ');


-- =====================================================
-- PRÉSENCE AUX ACTIVITÉS
-- =====================================================
INSERT INTO activity_attendance
(marked_by_user_id, registration_id, marked_at, note, status)
VALUES
(6,1,'2026-04-01 12:05:00','Arrivé à l''heure','PRÉSENT'),
(6,2,'2026-04-02 12:10:00','Participation active','PRÉSENT'),
(7,3,'2026-04-03 16:20:00','Très motivé','PRÉSENT'),
(7,4,'2026-04-04 17:15:00','Absence signalée par les parents','ABSENT'),
(6,5,'2026-04-05 18:25:00','Bonne participation','PRÉSENT'),
(6,6,'2026-04-06 12:00:00','Très concentré','PRÉSENT'),
(7,7,'2026-04-07 12:15:00','En retard de 10 minutes','PRÉSENT'),
(7,8,'2026-04-08 12:40:00','Participation correcte','PRÉSENT'),
(6,9,'2026-04-09 16:10:00','Absent pour voyage','ABSENT'),
(6,10,'2026-04-10 16:35:00','Très enthousiaste','PRÉSENT');


-- =====================================================
-- HISTORIQUE DES CHANGEMENTS D'INSCRIPTION
-- =====================================================
INSERT INTO activity_registration_log
(changed_by_user_id, registration_id, changed_at, new_status, old_status, note)
VALUES
(1,1,'2025-11-12 10:10:00','CONFIRMÉ','EN_ATTENTE','Validation par administrateur'),
(1,2,'2024-10-03 15:00:00','EN_ATTENTE',NULL,'Inscription initiale'),
(1,3,'2025-03-18 12:05:00','CONFIRMÉ','EN_ATTENTE','Place disponible'),
(1,4,'2024-12-28 09:20:00','ANNULÉ','CONFIRMÉ','Annulation demandée par le parent'),
(1,5,'2025-07-02 10:30:00','CONFIRMÉ','EN_ATTENTE','Inscription validée'),
(1,6,'2025-02-14 14:10:00','CONFIRMÉ','EN_ATTENTE','Confirmation automatique'),
(1,7,'2024-09-21 11:05:00','EN_ATTENTE',NULL,'Liste d''attente'),
(1,8,'2025-05-29 16:00:00','CONFIRMÉ','EN_ATTENTE','Validation du système'),
(1,9,'2024-11-08 18:20:00','CONFIRMÉ','EN_ATTENTE','Place confirmée'),
(1,10,'2025-01-19 13:10:00','CONFIRMÉ','EN_ATTENTE','Inscription validée');
