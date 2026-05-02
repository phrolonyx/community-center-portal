import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ChildService {

  private apiUrl = 'http://localhost:8080/api/children';

  constructor(private http: HttpClient) {}


}
