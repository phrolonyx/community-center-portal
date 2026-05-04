import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  // Récupérer tous les utilisateurs (admin)
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, { withCredentials: true });
  }
  // Récupérer tous les utilisateurs (admin seulement)
  getUsersByRole(role: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/role/${role}`,{ withCredentials: true });
  }

  // Rechercher des utilisateurs par nom ou courriel (admin)
  search(query: string): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, {
      params: { search: query },
      withCredentials: true
    });
  }

  // Récupérer un utilisateur par son id (admin)
  getById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`, { withCredentials: true });

  }

  // Activer un compte désactivé (admin)
  enableUser(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/enable`, {}, { withCredentials: true }
    );
  }

  // Désactiver un compte actif — impossible sur un ADMIN
  disableUser(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/disable`, {}, { withCredentials: true }
    );
  }

  // Approuver un compte en attente (admin)
  approveUser(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/approve`, {}, { withCredentials: true }
    );
  }

  // Rejeter un compte en attente (admin)
  rejectUser(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/reject`, {}, { withCredentials: true }
    );
  }
}
