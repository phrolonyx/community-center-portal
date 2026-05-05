import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ActivityRegistration {
  id: number;
  status: 'EN_ATTENTE' | 'CONFIRMÉ' | 'ANNULÉ';
  createdAt: string;
  confirmedAt: string | null;
  cancelledAt: string | null;
  childId: number;
  childFullName: string;
  activitySessionId: number;
  activityName: string;
  sessionStart: string;
  sessionEnd: string;
  locationName: string;
  hostFullName: string | null;
  parentProfileId: number;
}

export interface RegistrationRequest {
  childId: number;
  activitySessionId: number;
}

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {

  private apiUrl = `${environment.apiUrl}/registrations`;

  constructor(private http: HttpClient) {}

  // Récupérer toutes les inscriptions du parent connecté
  getMyRegistrations(): Observable<ActivityRegistration[]> {
    return this.http.get<ActivityRegistration[]>(this.apiUrl, { withCredentials: true });
  }

  // Inscrire un enfant à une séance
  register(data: RegistrationRequest): Observable<ActivityRegistration> {
    return this.http.post<ActivityRegistration>(this.apiUrl, data, { withCredentials: true });
  }

  // Annuler une inscription existante (côté parent)
  cancel(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/cancel`, {}, { withCredentials: true }
    );
  }

  // Récupérer toutes les inscriptions EN_ATTENTE (admin)
  getPending(): Observable<ActivityRegistration[]> {
    return this.http.get<ActivityRegistration[]>(`${this.apiUrl}/pending`, { withCredentials: true });
  }

  // Admin confirme une inscription -> envoie un email au parent
  adminConfirm(id: number): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/admin-confirm`, {}, { withCredentials: true }
    );
  }

  // Admin annule une inscription -> envoie un email au parent (avec motif optionnel)
  adminCancel(id: number, motif?: string): Observable<{ message: string }> {
    return this.http.put<{ message: string }>(
      `${this.apiUrl}/${id}/admin-cancel`,
      { motif: motif || '' },
      { withCredentials: true }
    );
  }
}
