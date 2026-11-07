# ---- Étape 1 : compilation d'Angular ----
# Angular 21 demande Node 20.19+, 22.12+ ou 24
FROM node:22-alpine AS build
WORKDIR /app

# Dépendances d'abord, pour profiter du cache de Docker
COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npx ng build --configuration production \
    # Le dossier de sortie contient le nom du projet (dist/<nom>/browser) :
    # on le déplace vers un chemin fixe pour ne pas dépendre de ce nom
    && mv dist/*/browser /out

# ---- Étape 2 : nginx sert les fichiers statiques et relaie /api ----
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /out /usr/share/nginx/html
EXPOSE 80
