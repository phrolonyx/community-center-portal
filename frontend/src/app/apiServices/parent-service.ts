import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ParentService {
  private apiUrl = 'http://localhost:8080/api/parent/profile';

  constructor(private http: HttpClient) {}

}
