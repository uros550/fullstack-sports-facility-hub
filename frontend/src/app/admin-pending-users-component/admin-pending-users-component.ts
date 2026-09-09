import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AdminService } from '../services/admin-service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-pending-users-component',
  imports: [CommonModule],
  templateUrl: './admin-pending-users-component.html',
  styleUrl: './admin-pending-users-component.css',
})
export class AdminPendingUsersComponent implements OnInit {

  currentUser: User | null = null;
  users: User[] = [];

  selectedUserId: number = 0;
  actionType: 'APPROVE' | 'REJECT' | null = null;

  successMessage: string = '';

  private adminService = inject(AdminService);

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
      this.adminService.getPendingUsers().subscribe(data => {
        this.users = data;
      })
    }
  }

  openAccept(userId: number) {
    this.selectedUserId = userId;
    this.actionType = 'APPROVE';
  }

  openReject(userId: number) {
    this.selectedUserId = userId;
    this.actionType = 'REJECT';
  }

  confirmAction() {
    if (this.actionType === 'APPROVE') {
      this.adminService.acceptRegistration(this.selectedUserId).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully approved user account';
          this.users = this.users.filter(u => u.id !== this.selectedUserId);
        } else {
          this.successMessage = 'Error while approving user account';
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      })
    }
    else if (this.actionType === 'REJECT') {
      this.adminService.rejectRegistration(this.selectedUserId).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully rejected user account';
          this.users = this.users.filter(u => u.id !== this.selectedUserId);
        } else {
          this.successMessage = 'Error while rejecting user account';
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      })
    }
  }

  cancelAction() {
    this.selectedUserId = 0;
    this.actionType = null;
  }

  clearMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

}
