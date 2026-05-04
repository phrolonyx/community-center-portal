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

  // Récupérer tous les utilisateurs (admin seulement)
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl, { withCredentials: true });
  }
  // Récupérer tous les utilisateurs (admin seulement)
  getUsersByRole(role: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/role/${role}`,{ withCredentials: true });
  }

  // Approuver un compte en attente
  approveUser(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/approve`, {}, { withCredentials: true });
  }



  // Rejeter un compte en attente
  rejectUser(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/reject`, {}, { withCredentials: true });
  }

  // Activer un compte désactivé
  enableUser(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/enable`, {}, { withCredentials: true });
  }

  // Désactiver un compte actif
  disableUser(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/disable`, {}, { withCredentials: true });
  }
}
