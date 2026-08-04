import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../services/user-service';
import { User } from '../models/User';
import { Reservation } from '../models/Reservation';
import { DatePipe } from '@angular/common';
import { Application } from '../models/Application';
import { FormsModule } from '@angular/forms';
import { AthleteProfile } from '../models/AthleteProfile';

@Component({
  selector: 'app-athlete-ads-component',
  imports: [DatePipe, FormsModule],
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
  activeReservationId: number = 0;
  myReservations: Reservation[] = [];
  myApplications: Application[] = [];
  showOptions: boolean = false;
  options: string[] = ['Accept', 'Reject'];
  selectedOption: string = 'Accept';
  selectedAthlete: AthleteProfile = new AthleteProfile();

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
        this.userService.getActiveReservationsByAthleteId(this.currentUser.id).subscribe(data => {
          this.myReservations = data;
        })
        this.userService.getAllApplicationsByAthlete(this.currentUser.id).subscribe(data => {
          this.myApplications = data;
        })
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

  anyActive(reservationId: number): boolean {
    return this.myApplications.some(
      a => a.reservationId === reservationId && a.status === 'PENDING'
    );
  }

  getApplications() {
    return this.myApplications.filter(a => a.reservationId === this.activeReservationId);
  }

  getAthlete(athleteId: number): AthleteProfile {
    this.userService.getProfileById(athleteId).subscribe(data => {
      this.selectedAthlete = data;
    })
    return this.selectedAthlete;
  }

  getProfilePicture(athlete: AthleteProfile) {
    if (athlete.profilePicture) {
      return 'http://localhost:8080/' + athlete.profilePicture;
    }
    return '/default-pfp.jpg';
  }

  check(reservationId: number) {
    this.activeReservationId = reservationId;
    this.selectedOption = 'Accept';
  }

  closeCheck() {
    //ako treba jos nesto
    this.activeReservationId = 0;
  }

  openOptions() {
    this.showOptions = true;
  }

  closeOptions() {
    //ako treba jos nesto
    this.showOptions = false;
  }

  confirmRespond() {
    //ako treba jos nesto
    this.closeOptions();
    this.changeStatus();
  }

  changeStatus() {
    //res id se nalazi u active reservation id
    //poziv za back kasnije
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
