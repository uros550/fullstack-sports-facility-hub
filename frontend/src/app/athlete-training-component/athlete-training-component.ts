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
import { Training } from '../models/Training';
import { NgClass } from '@angular/common';

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
  //search
  selectedSportId: number = 0;
  selectedFacilityId: number = 0;
  //buttons
  showArchive: boolean = false;

  private trainerService = inject(TrainerService);
  private trainingService = inject(TrainingService);
  private facilityService = inject(SportsFacilityService);
  private sportService = inject(SportService);


  ngOnInit(): void {
    const userJson = localStorage.getItem('loggedUser');
    const search = history.state.searchRequest;
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
        if (search) {
          this.selectedFacilityId = search.selectedFacilityId;
          this.selectedSportId = search.selectedSportId;
          this.searchTrainers(); //restore previous search
        }
      })

      this.sportService.getAllSports().subscribe(data => {
        this.allSports = data;
      })
      this.facilityService.getAllActiveFacilities().subscribe(data => {
        this.allFacilities = data;
      })
      this.trainingService.getAllTrainingsById(this.currentUser.id).subscribe(data => {
        this.myTrainings = data;
      })
    }
  }

  searchTrainers() {

    const request = {
      sportId: this.selectedSportId,
      facilityId: this.selectedFacilityId
    };

    this.trainerService.searchTrainers(request).subscribe(data => {
      this.displayedTrainers = data;
    })

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

  closeArchive() {
    this.showArchive = false;
  }

}
