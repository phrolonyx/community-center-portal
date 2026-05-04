import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class LocationService {

  private apiUrl = `${environment.apiUrl}/locations`;

  constructor(private http: HttpClient) {}

  // Récupérer tous les utilisateurs (admin seulement)
  getAllLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(this.apiUrl, { withCredentials: true });
  }

}
