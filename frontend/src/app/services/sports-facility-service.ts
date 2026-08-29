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

  getTop3Facilities() {
    return this.http.get<SportsFacility[]>(`${this.path}/top3`);
  }

  getFacilityById(id: number) {
    return this.http.get<SportsFacility>(`${this.path}/${id}`);
  }

  getFacilitiesForEmployee(employeeId: number) {
    return this.http.get<SportsFacility[]>(`${this.path}/employee/${employeeId}`);
  }

  getActiveFacilitiesCount() {
    return this.http.get<number>(`${this.path}/count`);
  }

  getAllCourtsById(facilityId: number) {
    return this.http.get<Court[]>(`${this.path}/courts/${facilityId}`);
  }

  getCourtsByFacilitySport(facilityId: number, sportId: number) {
    return this.http.get<Court[]>(`${this.path}/courts/${facilityId}/${sportId}`);
  }

  getFacilityImagesById(facilityId: number) {
    return this.http.get<string[]>(`${this.path}/images/${facilityId}`);
  }

  getAllCities() {
    return this.http.get<string[]>(`${this.path}/cities`);
  }

  searchFacilities(request: any) {
    return this.http.post<SportsFacility[]>(`${this.path}/search`, request);
  }

  updateFacility(facility: SportsFacility, courts: Court[], images: string[]) {
    
  }

  addFacility(newFacility: SportsFacility, courts: Court[], images: string[]) {

  }
  
}
