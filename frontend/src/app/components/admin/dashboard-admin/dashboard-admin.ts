import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard-admin',
  standalone: false,
  templateUrl: './dashboard-admin.html',
  styleUrl: './dashboard-admin.css',
})
export class DashboardAdmin {
  //  Valeurs statiques pour l'instant
  //  À remplacer par des valeurs venant de ton API Spring Boot
  totalUsers = 0;
  totalParents = 0;
  totalAnimateurs = 0;
  totalDisabled = 0;

  //  Liste des utilisateurs en attente
  //  À remplacer par un appel API
  pendingUsers = [
    // Exemple temporaire
    // {
    //   id: 1,
    //   firstName: 'Jean',
    //   lastName: 'Dupont',
    //   email: 'jean@example.com',
    //   role: 'PARENT',
    //   createdAt: new Date()
    // }
  ];

  //  Méthode pour approuver un utilisateur
  //  À connecter à ton service API
  approveUser(id: number) {
    console.log("Approuver utilisateur ID:", id);
    // TODO: appeler ton endpoint Spring Boot
  }

  //  Méthode pour refuser un utilisateur
  rejectUser(id: number) {
    console.log("Refuser utilisateur ID:", id);
    // TODO: appeler ton endpoint Spring Boot
  }

}
