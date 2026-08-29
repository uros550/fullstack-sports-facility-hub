import { Component, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AdminUsersComponent } from '../admin-users-component/admin-users-component';
import { AdminPendingUsersComponent } from '../admin-pending-users-component/admin-pending-users-component';

@Component({
  selector: 'app-admin-component',
  imports: [AdminUsersComponent, AdminPendingUsersComponent],
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
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }

}
