import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { SportsFacility } from '../models/SportsFacility';
import { Court } from '../models/Court';

@Injectable({
  providedIn: 'root',
})
export class SportsFacilityService {

  private http = inject(HttpClient);
  private path = 'http://localhost:8080/sportsFacilities';

  getAllFacilities() {
    return this.http.get<SportsFacility[]>(this.path);
  }

  getAllActiveFacilities() {
    return this.http.get<SportsFacility[]>(`${this.path}/active`);
  }

  getActiveFacilitiesCount() {
    return this.http.get<number>(`${this.path}/count`);
  }

  getTop3Facilities() {
    return this.http.get<SportsFacility[]>(`${this.path}/top3`);
  }

  getAllCities() {
    return this.http.get<string[]>(`${this.path}/cities`);
  }

  searchFacilities(request: any) {
    return this.http.post<SportsFacility[]>(`${this.path}/search`, request);
  }

  getFacilityById(id: number) {
    return this.http.get<SportsFacility>(`${this.path}/${id}`);
  }

  getAllCourtsById(facilityId: number) {
    return this.http.get<Court[]>(`${this.path}/courts/${facilityId}`);
  }
  
}
