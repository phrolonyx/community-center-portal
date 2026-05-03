import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

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

  // Récupérer les séances d'une activité spécifique
  getSessionsByActivity(activityId: number): Observable<ActivitySession[]> {
    return this.http.get<ActivitySession[]>(
      `${this.apiUrl}/activity/${activityId}`,
      { withCredentials: true }
    );
  }

  // Créer une nouvelle séance (admin)
  createSession(data: any): Observable<ActivitySession> {
    return this.http.post<ActivitySession>(this.apiUrl, data, { withCredentials: true });
  }

  // Modifier une séance existante (admin)
  updateSession(id: number, data: any): Observable<ActivitySession> {
    return this.http.put<ActivitySession>(`${this.apiUrl}/${id}`, data, { withCredentials: true });
  }

  // Supprimer une séance (admin)
  deleteSession(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }
}
