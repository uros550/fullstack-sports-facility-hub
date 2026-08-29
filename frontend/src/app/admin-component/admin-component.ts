import { Component, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AdminUsersComponent } from '../admin-users-component/admin-users-component';
import { AdminPendingUsersComponent } from '../admin-pending-users-component/admin-pending-users-component';
import { AdminFacilitiesComponent } from '../admin-facilities-component/admin-facilities-component';

@Component({
  selector: 'app-admin-component',
  imports: [AdminUsersComponent, AdminPendingUsersComponent, AdminFacilitiesComponent],
  templateUrl: './admin-component.html',
  styleUrl: './admin-component.css',
})
export class AdminComponent implements OnInit {
  
  currentUser: User | null = null;
  activeTab: string = 'users'; //default on open

  ngOnInit() {
    const userJson = localStorage.getItem('loggedUser');

    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
        
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    //get back after details
    if (history.state.section === 'facilities') {
      this.activeTab = 'facilities';
    }
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }

}
