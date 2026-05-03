import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;

  // Signal global pour l'utilisateur connecté — accessible dans toute l'app
  currentUser = signal<User | null>(null);

  // Valeur calculée automatiquement depuis currentUser
  isAuthenticated = computed(() => this.currentUser() !== null);

  constructor(private http: HttpClient) {}

  // Connexion — envoie email + mot de passe, reçoit l'objet User et met à jour le signal
  login(email: string, password: string): Observable<User> {
    return this.http.post<User>(
      `${this.apiUrl}/login`,
      { email, password },
      { withCredentials: true }
    ).pipe(
      tap(user => this.currentUser.set(user))
    );
  }

  // Déconnexion — invalide la session côté serveur et vide le signal
  logout(): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/logout`,
      {},
      { withCredentials: true }
    ).pipe(
      tap(() => this.currentUser.set(null))
    );
  }

  // Inscription — crée un nouveau compte (en attente d'approbation admin)
  register(data: any): Observable<User> {
    return this.http.post<User>(
      `${this.apiUrl}/register`,
      data,
      { withCredentials: true }
    );
  }

  // Appelée au démarrage dans AppComponent pour restaurer la session existante
  me(): Observable<User> {
    return this.http.get<User>(
      `${this.apiUrl}/me`,
      { withCredentials: true }
    ).pipe(
      tap(user => this.currentUser.set(user))
    );
  }

  // Vérification AJAX — utilisée dans le formulaire d'inscription pour éviter les doublons
  checkEmail(email: string): Observable<{ exists: boolean }> {
    return this.http.get<{ exists: boolean }>(
      `${this.apiUrl}/check-email`,
      { params: { email }, withCredentials: true }
    );
  }

  // Vérifie si l'utilisateur connecté possède un rôle précis
  hasRole(role: string): boolean {
    return this.currentUser()?.roleName === role;
  }
}
