import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { SportReservedStats } from '../models/SportReservationStats';
import { MonthlyActivity } from '../models/MonthlyActivity';
import { EquipmentSpending } from '../models/EquipmentSpending';

@Injectable({
  providedIn: 'root',
})
export class StatsService {
  
  private http = inject(HttpClient);
  private path = 'http://localhost:8080/stats';  

  getPlayedReservedPerSport() {
    return this.http.get<SportReservedStats[]>(`${this.path}/played/reserved/perSport`);
  }

  getMonthlyActivity() {
    return this.http.get<MonthlyActivity[]>(`${this.path}/monthly/activity`);
  }

  getEquipmentSpending() {
    return this.http.get<EquipmentSpending[]>(`${this.path}/monthly/equipment/spending`);
  }

}
