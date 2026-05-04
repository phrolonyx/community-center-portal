import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ParentProfile {
  id: number;
  phoneNumber: string;
  addressLine1: string;
  city: string;
  province: string;
  postalCode: string;
  country: string;
  emergencyName: string | null;
  emergencyPhone: string | null;
  emergencyRelationship: string | null;
}

export interface UpdateProfileRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  addressLine1?: string;
  city?: string;
  province?: string;
  emergencyName?: string;
  emergencyRelationship?: string;
  emergencyPhone?: string;
}

@Injectable({
  providedIn: 'root',
})
export class ParentService {

  private apiUrl = `${environment.apiUrl}/parent/profile`;

  constructor(private http: HttpClient) {}

  // Récupérer le profil du parent connecté
  getProfile(): Observable<ParentProfile> {
    return this.http.get<ParentProfile>(this.apiUrl, { withCredentials: true });
  }

  // Mettre à jour le profil du parent connecté — tous les champs sont optionnels
  updateProfile(data: UpdateProfileRequest): Observable<ParentProfile> {
    return this.http.put<ParentProfile>(this.apiUrl, data, { withCredentials: true });
  }
}
