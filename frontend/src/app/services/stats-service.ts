import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { SportReservedStats } from '../models/SportReservationStats';

@Injectable({
  providedIn: 'root',
})
export class StatsService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/stats';  

  getPlayedReservedPerSport() {
    return this.http.get<SportReservedStats[]>(`${this.path}/played/reserved/perSport`);
  }

}
