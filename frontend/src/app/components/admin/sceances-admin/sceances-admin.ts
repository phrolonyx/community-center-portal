import { Component } from '@angular/core';

@Component({
  selector: 'app-sceances-admin',
  standalone: false,
  templateUrl: './sceances-admin.html',
  styleUrl: './sceances-admin.css',
})
export class SceancesAdmin {
    //  Liste des séances (à remplacer par API)
  sessions = [
    // Exemple :
    // {
    //   id: 1,
    //   activityName: "Danse",
    //   hostName: "Sophie Martin",
    //   locationName: "Salle A",
    //   capacity: 20,
    //   dateTimeStart: new Date(),
    //   dateTimeEnd: new Date()
    // }
  ];

  // Supprimer séance
  deleteSession(id: number) {
    console.log("Supprimer séance ID:", id);
    // TODO: API delete
  }
}
