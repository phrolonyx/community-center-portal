import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Child {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string | null;
  age: number;
}

@Injectable({
  providedIn: 'root',
})
export class ChildService {

  private apiUrl = `${environment.apiUrl}/children`;

  constructor(private http: HttpClient) {}

  // Récupérer les enfants du parent connecté
  getMyChildren(): Observable<Child[]> {
    return this.http.get<Child[]>(this.apiUrl, { withCredentials: true });
  }

  // Récupérer tous les enfants (admin seulement)
  getAllChildren(): Observable<Child[]> {
    return this.http.get<Child[]>(`${this.apiUrl}`, { withCredentials: true });
  }

  // Créer un nouvel enfant
  createChild(data: any): Observable<Child> {
    return this.http.post<Child>(this.apiUrl, data, { withCredentials: true });
  }

  // Modifier un enfant existant
  updateChild(id: number, data: any): Observable<Child> {
    return this.http.put<Child>(`${this.apiUrl}/${id}`, data, { withCredentials: true });
  }

  // Supprimer un enfant
  deleteChild(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
