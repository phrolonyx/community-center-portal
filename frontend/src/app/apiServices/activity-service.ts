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
}
