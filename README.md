# CRM pour Manara

## Instructions:

Dans ./manara/frontend/
ng serve --proxy-config proxy.conf.json

Dans ./manara/backend/
./mvnw spring-boot:run

## Description

CRM pour l'organisme Manara, qui permet aux parents d'inscrire leurs enfants à des activités pré-établies par l'organisme.


## Structure du projet

Ce README sera mis à jour au fil de l'avancement du projet ainsi qu'en fonction des besoins auxquels nous ferons face. Éventuellement nous archiverons cette version de travail et en ferons une version finale qui contiendra les instructions de configuration, d'installation et de démarrage pour Manara.


## Détails techniques à propos du repo

### Branches

Nous utiliserons une branche `develop` sur laquelle nous pourrons merge dès qu'un issue sera complété dans sa branche respective et qui nous permettra d'avoir rapidement accès au travail de nos coéquipiers. Kristopher et Jean-Philippe s'occuperont de valider les merge requests. En cas de conflit complèxe à régler, nous pourrons optionnellement créer une branche avec le préfixe `merge/` suivi d'une courte description de la source du conflit avant de merge avec `develop`.

Les branches que nous utiliserons auront le format `type/CATÉGORIE-#-du-issue-description-en-kebab-case`. Les `types` principaux seront `feature` pour le développement des fonctionnalités, `task` pour tout changement qui a trait à la configuration ou la gestion de l'environnement de travail, `bugfix` pour la correction de bugs et nous pourrons en inventer en fonction des situations qui se présenteront à nous.


### Issues

Chaque issue respectera un format homogène dans le but d'améliorer la clarté. Voici le format des éléments récurrents:

| Élément | Format / Valeurs |
| :--- | :--- |
| **Titre** | `[CATÉGORIE]` suivi d'une brève description |
| **Catégories** | `[SETUP]`, `[ENTITÉ]`, `[REPO]`, `[SERVICE]`, `[CONTROLLER + VUE]`, `[TEST]`, `[INTÉGRATION]`, `[LIVRAISON]` |
| **Colonnes du kanban** | `À faire` → `En développement` → `Complété` |
| **Labels** | `setup`, `backend`, `frontend`, `test`, `db` |


### Commits

Les messages de commit doivent expliquer clairement ce qui a été gagné par l'intervention sans décrire inutilement *comment* ça a été fait. Cependant, si ce n'est pas clair, il peut être utile d'expliquer *pourquoi*. Idéalement le format sera `titre: explication concise`. Voici quelques exemples afin de donner une idée:

- `feat(model): créer l'entité Utilisateur`
- `docs: updater le README afin de clarifier le format des messages de commit`
- `fix(service): résoudre le NullPointerException sur une entrée vide de nom d'utilisateur`
- `refactor: déplacer la logique de confirmation par courriel dans la couche service`
- `style: séparer le toString d'Utilisateur sur plusieurs lignes`

Quelques titres récurrents dans les messages de commit: `feat:`, `fix:`, `build:`, `chore:`, `docs:`, `style:`, `refactor:`, `perf:`, `test:`.

En cas de doute, nous pouvons nous référer à la spécification [Conventional Commit](https://www.conventionalcommits.org/en/v1.0.0/), qui est très utile.


## Technologies utilisées

- Spring Boot
- Spring JPA
- MySQL
- Thymeleaf

## Questions

Pour toute question, n'hésitez pas à demander à Jean-Philippe ou Kristopher.
