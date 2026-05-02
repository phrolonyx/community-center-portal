import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ActivitySessionService {
  private apiUrl = 'http://localhost:8080/api/sessions';

  constructor(private http: HttpClient) {}


}
