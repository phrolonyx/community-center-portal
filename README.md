# Centre Communautaire — Gestion d'activités pour enfants

Application web de gestion pour un centre communautaire d'environ 300 membres. Les parents y inscrivent leurs enfants à des activités, les animateurs suivent leurs séances et l'administration gère l'ensemble : comptes, activités, séances et inscriptions.

> **Projet scolaire** réalisé en équipe dans le cadre du cours *420-G26-RO* au *Cegep de Rosemont*, session *4*. Le projet a d'abord été construit avec Thymeleaf, puis le frontend a été migré vers Angular et le backend transformé en API REST.

![Capture d'écran de l'accueil](docs/screenshots/accueil.png)

---

## Démarrage rapide

Seul [Docker](https://docs.docker.com/get-docker/) est nécessaire.

```bash
git clone https://github.com/Phrolonyx/community-center-portal.git
cd community-center-portal
cp .env.example .env
docker compose up --build
```

| Service | Adresse |
| :--- | :--- |
| Application | http://localhost:8080 |
| Courriels envoyés (Mailpit) | http://localhost:8025 |

Le premier lancement prend quelques minutes (téléchargement des dépendances Maven et npm). La base de données est créée et remplie automatiquement avec des données de démonstration.

### Comptes de démonstration

| Rôle | Courriel | Mot de passe |
| :--- | :--- | :--- |
| Administrateur | `admin@admin.com` | `admin123` |
| Parent | `sophie.tremblay@example.com` | `password` |
| Animatrice | `nadia.hassan@example.com` | `password` |

Les autres comptes de démo utilisent aussi `password`. Aucun courriel ne quitte la machine : ils sont tous captés par Mailpit.

---

## Fonctionnalités

**Visiteurs**
- Page d'accueil et catalogue public des activités
- Création d'un compte parent, à faire approuver par l'administration

**Parents**
- Gestion des enfants (lien de parenté, conditions médicales) et du profil
- Inscription d'un enfant à une séance, puis suivi ou annulation (en attente, confirmée, annulée)
- Boîte de réception des messages du centre, avec compteur de non-lus

**Animateurs**
- Liste de leurs séances et détail de chacune
- Consultation des enfants
- Messagerie : envoi à un parent, à plusieurs parents, à tous les parents ou aux administrateurs

**Administration**
- Tableau de bord : utilisateurs par rôle, comptes désactivés et en attente
- Approbation ou refus des nouveaux comptes, activation et désactivation, création de comptes
- Gestion des activités (avec téléversement d'image) et des séances (lieu, animateur, capacité, tranche d'âge)
- Confirmation ou annulation des inscriptions avec motif, annulation d'une séance entière
- Messagerie, avec boîte de réception et messages envoyés

**Courriels automatiques**
- Bienvenue, approbation du compte
- Inscription enregistrée, confirmée ou annulée ; séance annulée
- Rappels 24 h et 2 h avant chaque séance, par une tâche planifiée qui s'exécute chaque minute

### Règles métier

L'API valide les inscriptions côté serveur : l'enfant doit appartenir au parent connecté, une même séance ne peut pas être réservée deux fois, l'âge de l'enfant doit respecter la tranche de la séance, et la confirmation est refusée quand la capacité est atteinte. Des annotations de validation personnalisées (`@IsChild`, `@ValidAgeActivitySession`) vérifient l'âge des enfants et la cohérence des tranches d'âge.

---

## Stack technique

| Couche | Technologies |
| :--- | :--- |
| Frontend | Angular 21 (modules non standalone), Signals, RxJS, `ChangeDetectionStrategy.OnPush`, guards fonctionnels par rôle |
| Backend | Java 21, Spring Boot 4, Spring Security (sessions, `@PreAuthorize`), Spring Data JPA / Hibernate, Bean Validation, Spring Mail, `@Scheduled` |
| Base de données | MySQL 8.4 |
| Infrastructure | Docker Compose, nginx (fichiers statiques + proxy `/api`), Mailpit |

```
Navigateur ──► nginx :8080 ──┬── /        → application Angular (fichiers statiques)
                             └── /api/*   → Spring Boot :8080 ──► MySQL
                                                  └──────────────► Mailpit (SMTP)
```

Comme nginx sert le frontend et relaie l'API sur la même origine, les cookies de session fonctionnent sans configuration CORS particulière.

---

## Développement local (sans Docker)

Prérequis : Java 21, Node.js 22+, MySQL 8 en marche.

1. Créer le fichier `.env` à partir de `.env.example` et y mettre le mot de passe MySQL local. Spring le lit automatiquement.
2. Lancer les deux applications :

```bash
./start.sh
```

Le frontend est servi sur http://localhost:4200 avec rechargement à chaud ; les appels `/api` sont relayés au backend par `proxy.conf.json`.

Pour envoyer de vrais courriels en local, remplir `MAIL_USERNAME` et `MAIL_PASSWORD` dans `.env` (pour Gmail, un *mot de passe d'application*).

---

## Structure du dépôt

```
backend/            API REST Spring Boot
frontend/           Application Angular
legacy-thymeleaf/   Ancienne version Thymeleaf, conservée comme trace de la migration
docker-compose.yml  Orchestration : MySQL, Mailpit, backend, frontend
```

---

## Limites connues

Il s'agit d'un projet scolaire, pas d'une application prête pour la production :

- La modification et la suppression d'un enfant (`PUT` / `DELETE /children/{id}`) ne vérifient pas que l'enfant appartient au parent connecté (vulnérabilité de type IDOR). La création d'inscriptions, elle, fait cette vérification.
- Les animateurs voient tous les enfants, et non seulement ceux inscrits à leurs séances.
- La protection CSRF est désactivée.
- Le schéma est géré par `ddl-auto=update` plutôt que par des migrations versionnées (Flyway, Liquibase).
- Il n'y a pas de tests automatisés pour le backend ; ceux du frontend sont ceux générés par Angular CLI.

---

## Travail d'équipe

Le projet a été planifié avec un processus inspiré des pratiques professionnelles :

- **Branches** : `develop` comme branche d'intégration, branches de travail nommées `type/CATÉGORIE-#issue-description` (`feature`, `task`, `bugfix`), revue des merge requests avant intégration.
- **Issues** : titres catégorisés (`[ENTITÉ]`, `[SERVICE]`, `[CONTROLLER + VUE]`, `[TEST]`…) et suivi sur un tableau kanban.
- **Commits** : convention [Conventional Commits](https://www.conventionalcommits.org/fr/v1.0.0/) (`feat:`, `fix:`, `refactor:`, `docs:`…).

### Équipe

- **Jean-Philippe Racine** — Chef d'équipe
- **Kristopher Solhtalab** — Assistant
- **Aymane Chaki** — Développeur
- **Daryl Lucienne Niko** — Développeuse

---

## Crédits

- Icônes : [Icons8](https://icons8.com)
