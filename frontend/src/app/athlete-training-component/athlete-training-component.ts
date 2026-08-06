import { Component, inject, OnInit } from '@angular/core';
import { Trainer } from '../models/Trainer';
import { Sport } from '../models/Sport';
import { SportsFacility } from '../models/SportsFacility';
import { User } from '../models/User';
import { TrainerService } from '../services/trainer-service';
import { FormsModule } from '@angular/forms';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportService } from '../services/sport-service';
import { TrainingService } from '../services/training-service';
import { UserService } from '../services/user-service';
import { Training } from '../models/Training';
import { NgClass } from '@angular/common';
import { Court } from '../models/Court';

@Component({
  selector: 'app-athlete-training-component',
  imports: [FormsModule, NgClass],
  templateUrl: './athlete-training-component.html',
  styleUrl: './athlete-training-component.css',
})
export class AthleteTrainingComponent implements OnInit {

  currentUser: User | null = null;
  displayedTrainers: Trainer[] = [];
  allSports: Sport[] = [];
  allFacilities: SportsFacility[] = [];
  myTrainings: Training[] = [];

  // search
  selectedSportId: number = 0;
  selectedFacilityId: number = 0;

  // trainer & courts
  selectedTrainer: Trainer | null = null;
  courts: Court[] = [];

  // reservation details
  reservedCourt: Court | null = null;
  minDate: string = new Date().toISOString().split('T')[0];
  reservedDate: string = '';
  reservationSlots: any[] = [];
  reservedStartTime: string = '';
  availableDurations: number[] = [];
  selectedDuration: number = 1;
  errorMessage: string = '';

  // buttons
  showArchive: boolean = false;
  showSchedule: boolean = false;
  showReserve: boolean = false;

  private trainerService = inject(TrainerService);
  private trainingService = inject(TrainingService);
  private facilityService = inject(SportsFacilityService);
  private sportService = inject(SportService);
  private userService = inject(UserService);

  ngOnInit(): void {
    const userJson = localStorage.getItem('loggedUser');
    if (userJson) {
      try {
        this.currentUser = JSON.parse(userJson);
      } catch (error) {
        console.error('Local storage getItem error:', error);
      }
    }

    if (this.currentUser) {
      this.trainerService.getAllActiveTrainers().subscribe(data => {
        this.displayedTrainers = data;
        this.searchTrainers();
      });

      this.sportService.getAllSports().subscribe(data => {
        this.allSports = data;
      });

      this.facilityService.getAllActiveFacilities().subscribe(data => {
        this.allFacilities = data;
      });

      this.trainingService.getAllTrainingsById(this.currentUser.id).subscribe(data => {
        this.myTrainings = data;
      });
    }
  }

  searchTrainers() {
    const request = {
      sportId: this.selectedSportId,
      facilityId: this.selectedFacilityId
    };

    this.trainerService.searchTrainers(request).subscribe(data => {
      this.displayedTrainers = data;
    });
  }

  getDate(startTime: string): string {
    if (!startTime) return '';
    const [datePart] = startTime.split('T'); 
    const [year, month, day] = datePart.split('-');
    return `${day}.${month}.${year}.`;
  }

  getTime(startTime: string, endTime: string) {
    if (!startTime || !endTime) return '';
      
    const formatTime = (isoString: string) => {
      const timePart = isoString.split('T')[1];
      return timePart ? timePart.substring(0, 5) : '';
    };

    const start = formatTime(startTime);
    const end = formatTime(endTime);

    return `${start}-${end}`;
  }

  openArchive() {
    this.showArchive = true;
  }

  openSchedule(trainer: Trainer) {
    this.selectedTrainer = trainer;
    this.showSchedule = true;
    this.facilityService.getCourtsByFacilitySport(trainer.facilityId, trainer.sportId).subscribe(data => {
      this.courts = data;
    });
  }

  close() {
    this.showArchive = false;
    this.showSchedule = false;
    this.selectedTrainer = null;
    this.closeReserve();
    this.errorMessage = '';
  }

  openReserve(court: Court) {
    this.reservedCourt = court;
    this.showReserve = true;
    this.reservedDate = this.minDate;
    this.errorMessage = '';
    this.loadReservationAvailability();
  }

  closeReserve() {
    this.showReserve = false;
    this.reservedCourt = null;
    this.reservationSlots = [];
    this.reservedStartTime = '';
    this.availableDurations = [];
    this.selectedDuration = 1;
  }

  loadReservationAvailability() {
    if (!this.reservedDate || !this.reservedCourt) return;

    this.userService.getCourtAvailability(this.reservedCourt.id, this.reservedDate).subscribe(data => {
      let slots = data;

      const todayStr = new Date().toISOString().split('T')[0];
      if (this.reservedDate === todayStr) {
        const nowStr = new Date().toTimeString().substring(0, 5);
        slots = data.filter(slot => slot.startTime >= nowStr);
      } 

      this.reservationSlots = slots.filter(slot => slot.available);
      if (this.reservationSlots.length > 0) {
        this.reservedStartTime = this.reservationSlots[0].startTime;
        this.changeReservationStartTime();
      } else {
        this.reservedStartTime = '';
        this.availableDurations = [];
      }
    });
  }

  changeReservationDate() {
    this.errorMessage = '';
    this.loadReservationAvailability();
  }

  changeReservationStartTime() {
    if (!this.reservedStartTime || this.reservationSlots.length === 0) {
      this.availableDurations = [];
      return;
    }

    const startIndex = this.reservationSlots.findIndex(s => s.startTime === this.reservedStartTime);
    if (startIndex === -1) {
      this.availableDurations = [1];
      this.selectedDuration = 1;
      return;
    }

    let maxHours = 1;
    for (let i = startIndex; i < this.reservationSlots.length - 1; i++) {
      const currentStart = parseInt(this.reservationSlots[i].startTime.split(':')[0], 10);
      const nextStart = parseInt(this.reservationSlots[i + 1].startTime.split(':')[0], 10);

      if (nextStart === currentStart + 1) {
        maxHours++;
      } else {
        break;
      }
    }

    this.availableDurations = Array.from({ length: maxHours }, (_, index) => index + 1);
    this.selectedDuration = 1;
  }

  confirmReservation() {
    if (!this.reservedCourt || !this.reservedDate || !this.reservedStartTime || !this.selectedTrainer || !this.currentUser) {
      this.errorMessage = 'Please select a valid data.';
      return;
    }

    const startHour = parseInt(this.reservedStartTime.split(':')[0], 10);
    const endHour = startHour + Number(this.selectedDuration); 
    //start and end times in correct syntax for backend
    const startSyntax = `${this.reservedDate}T${String(startHour).padStart(2, '0')}:00:00`; 
    const endSyntax = `${this.reservedDate}T${String(endHour).padStart(2, '0')}:00:00`;

    const training: Training = new Training();
    training.trainerId = this.selectedTrainer.id;
    training.trainerName = `${this.selectedTrainer.firstName} ${this.selectedTrainer.lastName}`;
    training.athleteId = this.currentUser.id;
    training.facilityId = this.reservedCourt.facilityId;
    training.facilityName = this.selectedTrainer.facilityName;
    training.courtId = this.reservedCourt.id;
    training.courtName = this.reservedCourt.name;
    training.startTime = startSyntax;
    training.endTime = endSyntax;
    training.price = this.selectedTrainer.pricePerHour * this.selectedDuration; //final price
    training.status = 'PENDING';

    this.trainingService.reserveTraining(training).subscribe(data => {
      if (data === 'Success') {
        this.errorMessage = 'Training reservation created, pending approval';
        //update front data
        this.myTrainings.push(training);
        this.loadReservationAvailability();
      }
      else {
        this.errorMessage = 'Training reservation error';
      }
    });

    this.closeReserve();
  }

}