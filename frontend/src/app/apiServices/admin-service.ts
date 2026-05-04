import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User } from '../models/user.model';

export interface AdminDashboard {
  totalUsers: number;
  totalParents: number;
  totalAnimateurs: number;
  totalDisabled: number;
  pendingUsers: User[];
}

@Injectable({
  providedIn: 'root',
})
export class AdminService {

  private apiUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient) {}

  // Récupérer les statistiques du tableau de bord admin
  getDashboard(): Observable<AdminDashboard> {
    return this.http.get<AdminDashboard>(`${this.apiUrl}/dashboard`, { withCredentials: true });
  }
}
