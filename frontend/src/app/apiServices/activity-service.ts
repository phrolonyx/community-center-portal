import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Activity} from '../models/activity.model';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ActivityService {

  constructor(private http: HttpClient) { }

  private apiUrl = 'http://localhost:8080/api/activities';

  getActivities(): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${this.apiUrl}`);
  }
}
