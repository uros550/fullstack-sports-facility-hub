import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../models/User';
import { SportsFacility } from '../models/SportsFacility';
import { SportsFacilityService } from '../services/sports-facility-service';
import { CourtOccupancyReport } from '../models/CourtOccupancyReport';
import { EquipmentTurnoverReport } from '../models/EquipmentTurnoverReport';
import { StatsService } from '../services/stats-service';

@Component({
  selector: 'app-employee-pdfcomponent',
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-pdfcomponent.html',
  styleUrl: './employee-pdfcomponent.css',
})
export class EmployeePDFComponent implements OnInit {

  currentUser: User | null = null;
  facilities: SportsFacility[] = [];
  selectedFacilityId: number = 0;
  selectedMonth: number = new Date().getMonth() + 1;
  selectedYear: number = new Date().getFullYear();

  months = [
    { value: 1, name: 'Januar' }, { value: 2, name: 'Februar' }, { value: 3, name: 'Mart' },
    { value: 4, name: 'April' }, { value: 5, name: 'Maj' }, { value: 6, name: 'Jun' },
    { value: 7, name: 'Jul' }, { value: 8, name: 'Avgust' }, { value: 9, name: 'Septembar' },
    { value: 10, name: 'Oktobar' }, { value: 11, name: 'Novembar' }, { value: 12, name: 'Decembar' }
  ];

  years: number[] = [2024, 2025, 2026, 2027];

  occupancyData: CourtOccupancyReport[] = [];
  equipmentData: EquipmentTurnoverReport[] = [];

  private facilityService = inject(SportsFacilityService);
  private statsService = inject(StatsService);
  
  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    if (this.currentUser) {
      this.loadFacilities();
    }
  }

  loadFacilities() {
    this.facilityService.getFacilitiesForEmployee(this.currentUser!.id).subscribe(data => {
      this.facilities = data;
      if (this.facilities.length > 0) {
        this.selectedFacilityId = this.facilities[0].id;
      }
      this.loadData();
    })
  }

  loadData() {
    this.loadCourtOccupancy();
    this.loadEquipmentTurnover();
  }

  loadCourtOccupancy() {
    if (!this.selectedFacilityId) return;

    this.statsService.getCourtOccupancy(this.selectedFacilityId, this.selectedYear, this.selectedMonth).subscribe(data => {
      this.occupancyData = data;
    })
  }

  loadEquipmentTurnover() {
    this.statsService.getEquipmentTurnover(this.selectedYear, this.selectedMonth).subscribe(data => {
      this.equipmentData = data;
    })
  }

  changeFilter() {
    this.loadData();
  }

}
