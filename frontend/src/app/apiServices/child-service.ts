import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

// Correspond à ChildResponseDto.MedicalInfo
export interface MedicalInfo {
  id: number;
  condition: string;
  severity: string;
  notes?: string;
}

// Correspond exactement à ChildResponseDto.java
export interface Child {
  id: number;
  firstName: string;
  lastName: string;
  dateOfBirth?: string;
  age: number;
  relationship?: string;
  medicalConditions?: MedicalInfo[];
}

export interface ChildRequest {
  firstName: string;
  lastName: string;
  dateOfBirth: string | null;
  medical?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ChildService {

  private apiUrl = `${environment.apiUrl}/children`;

  constructor(private http: HttpClient) {}

  // Récupérer les enfants — admin reçoit tous, parent reçoit les siens
  getChildren(search?: string): Observable<Child[]> {
    const params: any = search ? { search } : {};
    return this.http.get<Child[]>(this.apiUrl, { params, withCredentials: true });
  }


  // Récupérer tous les enfants (admin seulement)
  getAllChildren(): Observable<Child[]> {
    return this.http.get<Child[]>(`${this.apiUrl}`, { withCredentials: true });
  }

  // Créer un nouvel enfant lié au parent connecté
  createChild(data: ChildRequest): Observable<Child> {
    return this.http.post<Child>(this.apiUrl, data, { withCredentials: true });
  }

  // Modifier un enfant existant
  updateChild(id: number, data: ChildRequest): Observable<Child> {
    return this.http.put<Child>(`${this.apiUrl}/${id}`, data, { withCredentials: true });
  }

  // Supprimer un enfant
  deleteChild(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
