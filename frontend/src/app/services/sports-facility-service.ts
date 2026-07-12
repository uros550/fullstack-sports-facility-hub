import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { SportsFacility } from '../models/SportsFacility';

@Injectable({
  providedIn: 'root',
})
export class SportsFacilityService {

  private http = inject(HttpClient);
  private path = 'http://localhost:8080/sportsFacilities';

  getAllFacilities() {
    return this.http.get<SportsFacility[]>(this.path);
  }
  
}
