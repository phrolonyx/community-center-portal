import { Component } from '@angular/core';

@Component({
  selector: 'app-form-sceance-admin',
  standalone: false,
  templateUrl: './form-sceance-admin.html',
  styleUrl: './form-sceance-admin.css',
})
export class FormSceanceAdmin {
    // Objet séance (à remplacer par API)
  session = {
    id: null,
    activityId: "",
    hostId: "",
    locationId: "",
    capacity: 1,
    minAge: null,
    maxAge: null,
    dateTimeStart: "",
    dateTimeEnd: ""
  };

  //  Listes dynamiques (à remplacer par API)
  activities = [];
  hosts = [];
  locations = [];

  constructor() {
    // TODO: charger via API
    // this.loadActivities();
    // this.loadHosts();
    // this.loadLocations();
  }

  // Sauvegarde séance
  saveSession() {
    console.log("Séance envoyée :", this.session);

    // TODO: envoyer à ton API Spring Boot
    // POST /admin/sessions/save
  }
}
