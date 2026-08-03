import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../services/user-service';
import { User } from '../models/User';
import { Reservation } from '../models/Reservation';
import { DatePipe } from '@angular/common';
import { Application } from '../models/Application';

@Component({
  selector: 'app-athlete-ads-component',
  imports: [DatePipe],
  templateUrl: './athlete-ads-component.html',
  styleUrl: './athlete-ads-component.css',
})
export class AthleteAdsComponent implements OnInit {
  //get mode from parent component and return to it
  @Input() mode: 'my' | 'explore' = 'my'; 
  @Output() backToReservations = new EventEmitter<void>();
  currentUser: User | null = null;
  exploreReservations: Reservation[] = [];
  applications: Application[] = [];

  successMessage: string = '';

  private userService = inject(UserService);

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
      
      if (this.mode === 'my') {
        
      } else {
        this.userService.getReservationAds(this.currentUser.id).subscribe(data => {
          this.exploreReservations = data;
        })
        this.userService.getAllApplicationsForAthlete(this.currentUser.id).subscribe(data => {
          this.applications = data;
        })
      }
    }
    
  }

  getStatus(reservationId: number): string {
    const app = this.applications.find(a => a.reservationId === reservationId);
    return app ? app.status : '';
  }

  apply(reservationId: number) {
    this.userService.applyToAd(reservationId, this.currentUser!.id).subscribe(data => {
      if (data) {
        this.successMessage = 'Successfully applied';
        this.applications.push({
          id: 0, //will be updated once its refreshed
          reservationId: reservationId,
          athleteId: this.currentUser!.id,
          status: 'PENDING'
        });
      }
      else {
        this.successMessage = 'Failed application'
      }
    })
  }

  goBack() {
    this.backToReservations.emit();
  }

}
