# ---- Étape 1 : compilation du .jar avec Maven et Java 21 ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copie du pom seul d'abord : les dépendances restent en cache
# tant que le pom ne change pas (builds suivants beaucoup plus rapides)
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

# ---- Étape 2 : image d'exécution légère (JRE seulement) ----
FROM eclipse-temurin:21-jre

# Relie l'image au dépôt GitHub (page du paquet sur GHCR)
LABEL org.opencontainers.image.source="https://github.com/phrolonyx/community-center-portal"

# Utilisateur sans privilèges : si l'application est compromise,
# l'attaquant n'est pas root dans le conteneur
RUN groupadd --system app && useradd --system --gid app --home-dir /app app

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Images de démo (profils, activités). Au premier lancement, Docker copie
# ce contenu dans le volume "uploads", qui persiste ensuite les téléversements.
# COPY uploads ./uploads

# Le dossier uploads appartient à "app" : un volume neuf hérite de ces droits
RUN mkdir -p /app/uploads && chown -R app:app /app

USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
