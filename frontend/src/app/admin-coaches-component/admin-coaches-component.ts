import { Component, inject, OnInit } from '@angular/core';
import { User } from '../models/User';
import { Trainer } from '../models/Trainer';
import { TrainerService } from '../services/trainer-service';

@Component({
  selector: 'app-admin-coaches-component',
  imports: [],
  templateUrl: './admin-coaches-component.html',
  styleUrl: './admin-coaches-component.css',
})
export class AdminCoachesComponent implements OnInit {
  
  currentUser: User | null = null;
  trainers: Trainer[] = [];

  selectedTrainerId: number = 0;
  actionType: 'ACTIVATE' | 'DEACTIVATE' | null = null;

  successMessage: string = '';

  private trainerService = inject(TrainerService);

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
      this.trainerService.getAllTrainers().subscribe(data => {
        this.trainers = data;
      })
    }
  }
  

  openActivate(trainerId: number) {
    this.selectedTrainerId = trainerId;
    this.actionType = 'ACTIVATE';
  }

  openDeactivate(trainerId: number) {
    this.selectedTrainerId = trainerId;
    this.actionType = 'DEACTIVATE';
  }

  confirmAction() {
    const trainer = this.trainers.find(f => f.id === this.selectedTrainerId);
    if (trainer) {
      this.trainerService.changeIsActive(trainer.id, !trainer.isActive).subscribe(data => {
        if (data) {
          this.successMessage = 'Successfully activated/deactivated trainer';
          trainer.isActive = !trainer.isActive;
        }
        else {
          this.successMessage = 'Error while activating/deactivating trainer';
        }
        this.cancelAction();
        this.clearMessageAfterDelay();
      })
    }
  }

  cancelAction() {
    this.selectedTrainerId = 0;
    this.actionType = null;
  }

  clearMessageAfterDelay() {
    setTimeout(() => {
      this.successMessage = '';
    }, 5000);
  }

}
