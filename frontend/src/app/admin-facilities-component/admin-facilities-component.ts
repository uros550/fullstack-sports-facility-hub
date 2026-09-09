import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { AdminService } from '../services/admin-service';
import { SportsFacility } from '../models/SportsFacility';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-facilities-component',
  imports: [],
  templateUrl: './admin-facilities-component.html',
  styleUrl: './admin-facilities-component.css',
})
export class AdminFacilitiesComponent implements OnInit {
  
  currentUser: User | null = null;
  facilities: SportsFacility[] = [];

  selectedFacilityId: number = 0;
  actionType: 'APPROVE' | 'REJECT' | null = null;

  successMessage: string = '';

  private adminService = inject(AdminService);
  private router = inject(Router);

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
      this.adminService.getPendingFacilities().subscribe(data => {
        this.facilities = data;
      })
    }
  }

  openAccept(facId: number) {
    this.selectedFacilityId = facId;
    this.actionType = 'APPROVE';
  }

  openReject(facId: number) {
    this.selectedFacilityId = facId;
    this.actionType = 'REJECT';
  }

  confirmAction() {
    if (this.actionType === 'APPROVE') {
      this.adminService.acceptFacility(this.selectedFacilityId).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully approved facility';
          this.facilities = this.facilities.filter(f => f.id !== this.selectedFacilityId);
        } else {
          this.successMessage = 'Error while approving facility';
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      })
    }
    else if (this.actionType === 'REJECT') {
      this.adminService.rejectFacility(this.selectedFacilityId).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully rejected facility';
          this.facilities = this.facilities.filter(f => f.id !== this.selectedFacilityId);
        } else {
          this.successMessage = 'Error while rejecting facility';
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      })
    }
  }

  cancelAction() {
    this.selectedFacilityId = 0;
    this.actionType = null;
  }

  clearMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

  viewDetails(facilityId: number) {
    this.router.navigate(['/facility', facilityId]);
  }

}
