import { Component, computed, OnInit, signal } from '@angular/core';
import { UserService } from '../../../apiServices/user-service';
import { User } from '../../../models/user.model';
import { ChangeDetectionStrategy } from '@angular/core';
import { AdminCreateUserRequest } from '../../../apiServices/user-service';
import { ChangeDetectorRef } from '@angular/core';

interface CreateUserForm {
  prenom: string;
  nom: string;
  email: string;
  password: string;
  confirmPassword: string;
  role: string;
}

@Component({
  selector: 'app-users-admin',
  standalone: false,
  templateUrl: './users-admin.html',
  styleUrl: './users-admin.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class UsersAdmin implements OnInit {

  constructor(
    private userService: UserService,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.loadUsers()
  }

  users = signal<User[]>([]);

  searchValue = signal('');

  modalOpen = signal(false);
  isLoading = signal(false);
  showPassword = false;
  showConfirm = false;
  serverError = signal('');
  successMessage = signal('');
  clientError = signal(false);

  form: CreateUserForm = {
    prenom: '',
    nom: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: ''
  };

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

  openModal(): void {
    this.form = { prenom: '', nom: '', email: '', password: '', confirmPassword: '', role: '' };
    this.serverError.set('');
    this.successMessage.set('');
    this.clientError.set(false);
    this.showPassword = false;
    this.showConfirm = false;
    this.modalOpen.set(true);
    this.cd.markForCheck();
  }

  closeModal(): void {
    this.modalOpen.set(false);
    this.cd.markForCheck();
  }

  togglePassword(champ: string): void {
    if (champ === 'password') this.showPassword = !this.showPassword;
    else this.showConfirm = !this.showConfirm;
  }

  // Validations du formulaire
  emailOk(v: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v.trim());
  }

  formulaireValide(): boolean {
    return this.form.prenom.trim().length >= 2
      && this.form.nom.trim().length >= 2
      && this.emailOk(this.form.email)
      && this.form.password.length >= 8
      && this.form.confirmPassword === this.form.password
      && this.form.role.length > 0;
  }

  // Soumettre la création d'un utilisateur
  onSubmit(): void {
    if (!this.formulaireValide()) {
      this.clientError.set(true);
      return;
    }

    this.clientError.set(false);
    this.serverError.set('');
    this.isLoading.set(true);

    const payload: AdminCreateUserRequest = {
      prenom: this.form.prenom.trim(),
      nom: this.form.nom.trim(),
      email: this.form.email.trim(),
      password: this.form.password,
      role: this.form.role,
    };

    // Créer l'utilisateur via l'API
    this.userService.createUser(payload).subscribe({
      next: (newUser) => {
        this.isLoading.set(false);
        // Ajouter le nouvel utilisateur à la liste locale sans recharger
        this.users.set([...this.users(), newUser]);
        this.successMessage.set(`Compte de ${newUser.firstName} ${newUser.lastName} créé avec succès.`);
        this.closeModal();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.serverError.set(err?.error?.error || 'Une erreur est survenue. Veuillez réessayer.');
      }
    });
  }

}
