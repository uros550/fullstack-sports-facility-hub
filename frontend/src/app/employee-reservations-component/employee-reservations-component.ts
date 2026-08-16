import { Component, inject, OnInit } from '@angular/core';
import { Reservation } from '../models/Reservation';
import { Training } from '../models/Training';
import { UserService } from '../services/user-service';
import { TrainingService } from '../services/training-service';
import { User } from '../models/User';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-employee-reservations-component',
  imports: [DatePipe],
  templateUrl: './employee-reservations-component.html',
  styleUrl: './employee-reservations-component.css',
})
export class EmployeeReservationsComponent implements OnInit {

  currentUser: User | null = null;
  showRes: boolean = true;
  reservations: Reservation[] = [];
  trainings: Training[] = [];

  private userService = inject(UserService);
  private trainingService = inject(TrainingService);
  private refreshTimer: any;

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
      this.loadData();

      this.refreshTimer = setInterval(() => {}, 30000); //every 30s refresh data
    }
  }

  loadData() {
    this.loadReservations();
    this.loadTrainings();
  }

  loadReservations() {
    this.userService.getActiveReservationsByEmployeeId(this.currentUser!.id).subscribe(data => {
      this.reservations = data;
    });
  }

  loadTrainings() {
    this.trainingService.getActiveTrainingsByEmployeeId(this.currentUser!.id).subscribe(data => {
      this.trainings = data;
    });
  }

  toggleView(isRes: boolean) {
    this.showRes = isRes;
  }

  acceptReservation(id: number) {
    this.userService.acceptReservation(id).subscribe(data => {
      this.loadData();
    })
  }

  rejectReservation(id: number) {
    this.userService.rejectReservation(id).subscribe(data => {
      this.loadData();
    })
  }

  noShowReservation(id: number, athleteId: number, facilityId: number) {
    this.userService.noShowReservation(id, athleteId, facilityId).subscribe(data => {
      this.loadData();
    })
  }

  acceptTraining(id: number) {
    this.trainingService.acceptTraining(id).subscribe(data => {
      this.loadData();
    })
  }

  rejectTraining(id: number) {
    this.trainingService.rejectTraining(id).subscribe(data => {
      this.loadData();
    })
  }

  noShowTraining(id: number, athleteId: number, facilityId: number) {
    this.trainingService.noShowTraining(id, athleteId, facilityId).subscribe(data => {
      this.loadData();
    })
  }

  canShowNoShow(startTime: string | Date): boolean {
    if (!startTime) return false;
    
    const start = new Date(startTime).getTime();
    const now = Date.now();
    const tenMinutesInMillis = 10 * 60 * 1000;

    //return true if started but 10 mins not passed
    return now >= start && now <= start + tenMinutesInMillis;
  }

}
