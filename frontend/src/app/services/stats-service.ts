import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { SportReservedStats } from '../models/SportReservationStats';
import { MonthlyActivity } from '../models/MonthlyActivity';
import { EquipmentSpending } from '../models/EquipmentSpending';
import { CourtOccupancyReport } from '../models/CourtOccupancyReport';
import { EquipmentTurnoverReport } from '../models/EquipmentTurnoverReport';

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

  getCourtOccupancy(facilityId: number, year: number, month: number) {
    return this.http.get<CourtOccupancyReport[]>(`${this.path}/court/occupancy`, {
      params: {
        facilityId: facilityId,
        year: year,
        month: month
      }
    });
  }

  getEquipmentTurnover(year: number, month: number) {
    return this.http.get<EquipmentTurnoverReport[]>(`${this.path}/equipment/turnover`, {
      params: {
        year: year,
        month: month
      }
    });
  }

}
