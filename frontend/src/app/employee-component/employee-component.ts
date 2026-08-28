import { Component, OnInit } from '@angular/core';
import { User } from '../models/User';
import { EmployeeProfileComponent } from '../employee-profile-component/employee-profile-component';
import { EmployeeReservationsComponent } from '../employee-reservations-component/employee-reservations-component';
import { EmployeePromotionsComponent } from '../employee-promotions-component/employee-promotions-component';
import { EmployeeCalendarComponent } from '../employee-calendar-component/employee-calendar-component';
import { EmployeePDFComponent } from '../employee-pdfcomponent/employee-pdfcomponent';
import { EmployeeFacilitiesComponent } from '../employee-facilities-component/employee-facilities-component';

@Component({
  selector: 'app-employee-component',
  imports: [EmployeeProfileComponent, EmployeeReservationsComponent, EmployeePromotionsComponent, EmployeeCalendarComponent, EmployeePDFComponent, EmployeeFacilitiesComponent],
  templateUrl: './employee-component.html',
  styleUrl: './employee-component.css',
})
export class EmployeeComponent implements OnInit {

  currentUser: User | null = null;
  activeTab: string = 'profile'; //default on open

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }

}