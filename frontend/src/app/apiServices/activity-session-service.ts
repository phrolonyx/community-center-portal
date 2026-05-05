import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

// Correspond exactement à ActivitySessionResponseDto.java
export interface ActivitySession {
  id: number;
  dateTimeStart: string;
  dateTimeEnd: string;
  capacity: number;
  activityId: number;
  activityName: string;
  locationId: number;
  locationName: string;
  hostId: number;
  hostFullName: string;
  minAge: number;
  maxAge: number;
  confirmedCount: number; // nombre d'enfants confirmés
}

export interface ActivitySessionRequest {
  activityId: number;
  hostId: number;
  locationId: number;
  capacity: number;
  dateTimeStart: string;
  dateTimeEnd: string;
}

@Injectable({
  providedIn: 'root',
})
export class ActivitySessionService {

  private apiUrl = `${environment.apiUrl}/sessions`;

  constructor(private http: HttpClient) {}

  // Récupérer toutes les séances (admin)
  getAllSessions(): Observable<ActivitySession[]> {
    return this.http.get<ActivitySession[]>(this.apiUrl, { withCredentials: true });
  }

  // Récupérer une séance par son id (admin)
  getById(id: number): Observable<ActivitySession> {
    return this.http.get<ActivitySession>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  // Créer une nouvelle séance (admin)
  createSession(data: ActivitySessionRequest): Observable<ActivitySession> {
    return this.http.post<ActivitySession>(this.apiUrl, data, { withCredentials: true });
  }

  // Modifier une séance existante (admin)
  updateSession(id: number, data: ActivitySessionRequest): Observable<ActivitySession> {
    return this.http.put<ActivitySession>(`${this.apiUrl}/${id}`, data, { withCredentials: true });
  }

  // Supprimer une séance (admin)
  deleteSession(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  // Annuler une séance (admin) — notifie tous les parents inscrits par email
  cancelSession(id: number): Observable<{ message: string; notifies: number }> {
    return this.http.put<{ message: string; notifies: number }>(
      `${this.apiUrl}/${id}/cancel`, {}, { withCredentials: true }
    );
  }

  getSessionById(id: number): Observable<ActivitySession> {
    return this.http.get<ActivitySession>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
