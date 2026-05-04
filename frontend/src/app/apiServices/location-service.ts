import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Location {
  id: number;
  name: string;
  address: string;
  capacity: number;
  description: string;
}

@Injectable({
  providedIn: 'root',
})
export class LocationService {

  private apiUrl = `${environment.apiUrl}/locations`;

  constructor(private http: HttpClient) {}

  // Récupérer tous les lieux disponibles (pour les formulaires admin)
  getAll(): Observable<Location[]> {
    return this.http.get<Location[]>(this.apiUrl, { withCredentials: true });
  }
}
