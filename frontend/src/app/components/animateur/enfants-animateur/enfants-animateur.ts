import { Component } from '@angular/core';

@Component({
  selector: 'app-enfants-animateur',
  standalone: false,
  templateUrl: './enfants-animateur.html',
  styleUrl: './enfants-animateur.css',
})
export class EnfantsAnimateur {
      // Recherche
  searchValue = "";

  // Liste des enfants (à remplacer par API)
  children = [
    // Exemple :
    // {
    //   id: 1,
    //   fullName: "Emma Dupont",
    //   dateOfBirth: new Date(2020, 4, 12),
    //   age: 4,
    //   parentLinks: [
    //     { parentName: "Jean Dupont" },
    //     { parentName: "Marie Dupont" }
    //   ],
    //   medicalConditions: ["Asthme"]
    // }
  ];

  // Recherche API
  searchChildren() {
    console.log("Recherche :", this.searchValue);
    // TODO: appeler ton API Spring Boot
  }

  // Supprimer enfant
  deleteChild(id: number) {
    console.log("Supprimer enfant ID:", id);
    // TODO: API delete
  }
}
