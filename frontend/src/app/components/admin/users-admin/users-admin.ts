import {Component, computed, OnInit, signal} from '@angular/core';
import {UserService} from '../../../apiServices/user-service';
import {User} from '../../../models/user.model';

@Component({
  selector: 'app-users-admin',
  standalone: false,
  templateUrl: './users-admin.html',
  styleUrl: './users-admin.css',
})

export class UsersAdmin implements OnInit{

  constructor(private userService : UserService) {}

  ngOnInit() {
    this.loadUsers()
  }

  users = signal<User[]>([]);

  searchValue = signal('');


  // Charger tous les utilisateurs
  loadUsers() {
    this.userService.getAllUsers().subscribe({
      next: data => this.users.set(data),
      error: () => this.users.set([])
    });
  }

  // 🔍 Recherche locale (nom, prénom, email)
  searchUsers = computed(() => {
    const search = this.searchValue().toLowerCase().trim();
    if (!search) return this.users();

    return this.users().filter(user =>
      user.firstName.toLowerCase().includes(search) ||
      user.lastName.toLowerCase().includes(search) ||
      user.email.toLowerCase().includes(search)
    );
  });

  // Désactiver utilisateur
  disableUser(id: number) {
    this.userService.disableUser(id).subscribe({
      next: () => {
        // Mettre à jour localement
        const updated = this.users().map(u =>
          u.id === id ? { ...u, enabled: false } : u
        );
        this.users.set(updated);
      },
      error: err => console.error("Erreur disable :", err)
    });
  }

// Activer utilisateur
  enableUser(id: number) {
    this.userService.enableUser(id).subscribe({
      next: () => {
        // Mettre à jour localement
        const updated = this.users().map(u =>
          u.id === id ? { ...u, enabled: true } : u
        );
        this.users.set(updated);
      },
      error: err => console.error("Erreur enable :", err)
    });
  }


  // // Modal message
  // modalOpen = false;
  // messageSent = false;
  //
  // messageRecipient = "";
  // messageType = "ANNONCE";
  // messageSubject = "";
  // messageContent = "";


  // Modal
  // openMessageModal() {
  //   this.modalOpen = true;
  // }
  //
  // closeMessageModal() {
  //   this.modalOpen = false;
  // }

  // Envoi message API
  // sendMessage() {
  //   console.log("Message envoyé :", {
  //     recipient: this.messageRecipient,
  //     type: this.messageType,
  //     subject: this.messageSubject,
  //     content: this.messageContent
  //   });
  //
  //   this.messageSent = true;
  //   this.modalOpen = false;


  }
