import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Activity} from '../models/activity.model';
import {Observable} from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ActivityService {

  constructor(private http: HttpClient) { }

  private apiUrl = `${environment.apiUrl}/activities`;

  getActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${this.apiUrl}`);
  }

  // Supprimer une séance (admin)
  deleteActivities(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { withCredentials: true });
  }

  getActivityById(id: number): Observable<Activity> {
    return this.http.get<Activity>(`${this.apiUrl}/${id}`);
  }
  // Créer une activité (multipart/form-data)
  createActivity(formData: FormData): Observable<Activity> {
    return this.http.post<Activity>(`${this.apiUrl}`, formData, {
      withCredentials: true
    });
  }

  // Modifier une activité (multipart/form-data)
  updateActivity(formData: FormData): Observable<Activity> {
    return this.http.put<Activity>(`${this.apiUrl}`, formData, {
      withCredentials: true
    });
  }

  createActivityJson(body: any) {
    return this.http.post<Activity>(`${this.apiUrl}`, body, {
      withCredentials: true
    });
  }

  updateActivityJson(id: number, body: any) {
    return this.http.put<Activity>(`${this.apiUrl}/${id}`, body, {
      withCredentials: true
    });
  }

  uploadImage(id: number, formData: FormData) {
    return this.http.post(`${this.apiUrl}/${id}/image`, formData, {
      withCredentials: true
    });
  }





}
