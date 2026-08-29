import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AdminService } from '../services/admin-service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-users-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-users-component.html',
  styleUrl: './admin-users-component.css',
})
export class AdminUsersComponent implements OnInit {

  currentUser: User | null = null;
  users: User[] = [];

  selectedUserId: number = 0;
  actionType: 'UPDATE' | 'DELETE' | null = null;
  editingUsername: string = '';

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
      this.adminService.getAllUsers().subscribe(data => {
        this.users = data;
      })
    }
  }

  openUpdate(user: User) {
    this.selectedUserId = user.id;
    this.actionType = 'UPDATE';
    this.editingUsername = user.username;
  }

  openDelete(userId: number) {
    this.selectedUserId = userId;
    this.actionType = 'DELETE';
    this.editingUsername = '';
  }

  confirmAction() {
    if (this.actionType === 'UPDATE') {
      this.adminService.changeUsername(this.selectedUserId, this.editingUsername).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully updated username';
          const user = this.users.find(u => u.id === this.selectedUserId);
          if (user) user.username = this.editingUsername;
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      })
    }
    else if (this.actionType === 'DELETE') {
      this.adminService.deleteAccount(this.selectedUserId).subscribe(success => {
        if (success) {
          this.successMessage = 'Successfully deleted user account';
          const user = this.users.find(u => u.id === this.selectedUserId);
          if (user) user.status = 'REJECTED';
        } else {
          this.successMessage = 'Error while deleting user account';
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      });
    }
  }

  cancelAction() {
    this.selectedUserId = 0;
    this.actionType = null;
    this.editingUsername = '';
  }

  clearMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

}
