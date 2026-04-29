import { Component } from '@angular/core';

@Component({
  selector: 'app-activites-admin',
  standalone: false,
  templateUrl: './activites-admin.html',
  styleUrl: './activites-admin.css',
})
export class ActivitesAdmin {
      // Liste des activités (à remplacer par API)
  activities = [
    // Exemple :
    // {
    //   id: 1,
    //   name: "Danse",
    //   description: "Cours de danse pour enfants"
    // }
  ];

  // Supprimer activité
  deleteActivity(id: number) {
    console.log("Supprimer activité ID:", id);
    // TODO: API delete
  }
}
