import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../models/User';
import { SportsFacility } from '../models/SportsFacility';
import { SportsFacilityService } from '../services/sports-facility-service';
import { CourtOccupancyReport } from '../models/CourtOccupancyReport';
import { EquipmentTurnoverReport } from '../models/EquipmentTurnoverReport';
import { StatsService } from '../services/stats-service';
//generate pdf
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';

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

  generatePDF() {
    const doc = new jsPDF();
    
    const facilityName = this.sanitizeText(this.facilities.find(f => f.id == this.selectedFacilityId)?.name || '');
    const monthName = this.sanitizeText(this.months.find(m => m.value == this.selectedMonth)?.name || '');

    doc.setFontSize(18);
    doc.setTextColor(15, 23, 42);
    doc.text('Reports and Statistics', 14, 20);

    doc.setFontSize(11);
    doc.setTextColor(51, 65, 85);
    doc.text(`Sports Facility: ${facilityName}`, 14, 28);
    doc.text(`Period: ${monthName} ${this.selectedYear}`, 14, 34);

    doc.setFontSize(14);
    doc.setTextColor(15, 23, 42);
    doc.text('Court Occupancy', 14, 46);

    autoTable(doc, {
      startY: 50,
      head: [['Court', 'Reserved Hours', 'Occupancy (%)']],
      body: this.occupancyData.map(item => [
        this.sanitizeText(item.courtName),
        `${item.reservedHours} h`,
        `${item.occupancyPercentage}%`
      ]),
      headStyles: { fillColor: [15, 23, 42], textColor: [255, 255, 255] },
      styles: { fontSize: 10, cellPadding: 4 }
    });

    const lastY = (doc as any).lastAutoTable.finalY || 50;

    doc.setFontSize(14);
    doc.setTextColor(15, 23, 42);
    doc.text('Equipment Turnover', 14, lastY + 14);

    autoTable(doc, {
      startY: lastY + 18,
      head: [['Equipment', 'Quantity', 'Total Revenue']],
      body: this.equipmentData.map(item => [
        this.sanitizeText(item.equipmentName),
        item.totalQuantitySold.toString(),
        `${item.totalRevenue.toFixed(2)} RSD`
      ]),
      headStyles: { fillColor: [15, 23, 42], textColor: [255, 255, 255] },
      styles: { fontSize: 10, cellPadding: 4 }
    });

    doc.save(`Report_${facilityName}_${monthName}_${this.selectedYear}.pdf`);
  }

  //remove serbian latin letters
  sanitizeText(str: string): string {
    if (!str) return '';
    return str
      .replace(/č/g, 'c').replace(/Č/g, 'C')
      .replace(/ć/g, 'c').replace(/Ć/g, 'C')
      .replace(/š/g, 's').replace(/Š/g, 'S')
      .replace(/ž/g, 'z').replace(/Ž/g, 'Z')
      .replace(/đ/g, 'dj').replace(/Đ/g, 'Dj');
  }

}
