import { Component } from '@angular/core';

@Component({
  selector: 'app-users-admin',
  standalone: false,
  templateUrl: './users-admin.html',
  styleUrl: './users-admin.css',
})
export class UsersAdmin {
  // Recherche
  searchValue = "";

  // Liste des utilisateurs (à remplacer par API)
  users = [
    // Exemple :
    // { id: 1, firstName: 'Jean', lastName: 'Dupont', role: 'PARENT', email: 'jean@example.com', enabled: true, lastLogin: new Date(), createdAt: new Date() }
  ];

  // Modal message
  modalOpen = false;
  messageSent = false;

  messageRecipient = "";
  messageType = "ANNONCE";
  messageSubject = "";
  messageContent = "";

  // Recherche API
  searchUsers() {
    console.log("Recherche :", this.searchValue);
    // TODO: appeler ton API Spring Boot
  }

  // Désactiver utilisateur
  disableUser(id: number) {
    console.log("Désactiver utilisateur :", id);
    // TODO: API disable
  }

  // Activer utilisateur
  enableUser(id: number) {
    console.log("Activer utilisateur :", id);
    // TODO: API enable
  }

  // Modal
  openMessageModal() {
    this.modalOpen = true;
  }

  closeMessageModal() {
    this.modalOpen = false;
  }

  // Envoi message API
  sendMessage() {
    console.log("Message envoyé :", {
      recipient: this.messageRecipient,
      type: this.messageType,
      subject: this.messageSubject,
      content: this.messageContent
    });

    this.messageSent = true;
    this.modalOpen = false;

    // TODO: API send message
  }
}
