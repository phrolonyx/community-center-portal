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
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Images de démo (profils, activités). Au premier lancement, Docker copie
# ce contenu dans le volume "uploads", qui persiste ensuite les téléversements.
# COPY uploads ./uploads

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
