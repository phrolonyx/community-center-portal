import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Activity } from '../models/activity.model';

export interface ActivityRequest {
  name: string;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class ActivityService {

  private apiUrl = `${environment.apiUrl}/activities`;

  constructor(private http: HttpClient) {}

  // Récupérer toutes les activités (public)
  getActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(this.apiUrl, { withCredentials: true });
  }

  // Récupérer une activité par son id (public)
  getById(id: number): Observable<Activity> {
    return this.http.get<Activity>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  // Créer une nouvelle activité (admin)
  create(data: ActivityRequest): Observable<Activity> {
    return this.http.post<Activity>(this.apiUrl, data, { withCredentials: true });
  }

  // Modifier une activité existante (admin)
  update(id: number, data: ActivityRequest): Observable<Activity> {
    return this.http.put<Activity>(`${this.apiUrl}/${id}`, data, { withCredentials: true });
  }

  // Supprimer une activité (admin)
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  // Uploader une image pour une activité (admin)
  uploadImage(id: number, file: File): Observable<{ imagePath: string }> {
    const formData = new FormData();
    formData.append('imageFile', file);
    return this.http.post<{ imagePath: string }>(
      `${this.apiUrl}/${id}/image`,
      formData,
      { withCredentials: true }
    );
  }
}
