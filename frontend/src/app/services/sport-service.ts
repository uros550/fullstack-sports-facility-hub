import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Sport } from '../models/Sport';

@Injectable({
  providedIn: 'root',
})

export class SportService {

  private http = inject(HttpClient);
  private path = 'http://localhost:8080/sports';

  getAllSports() {
    return this.http.get<Sport[]>(this.path);
  }

  getMaxMissingPlayers(sportId: number) {
    return this.http.get<number>(`${this.path}/requiredPlayers/${sportId}`);
  }

  getSportsByFacilityId(facilityId: number) {
    return this.http.get<Sport[]>(`${this.path}/facility/${facilityId}`);
  }

  addSport(newSport: Sport) {
    return this.http.post<boolean>(`${this.path}/add`, newSport);
  }

}
