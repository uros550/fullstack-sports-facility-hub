import { Component, inject, OnInit } from '@angular/core';
import { Trainer } from '../models/Trainer';
import { Sport } from '../models/Sport';
import { SportsFacility } from '../models/SportsFacility';
import { User } from '../models/User';
import { TrainerService } from '../services/trainer-service';
import { FormsModule } from '@angular/forms';
import { SportsFacilityService } from '../services/sports-facility-service';
import { SportService } from '../services/sport-service';

@Component({
  selector: 'app-athlete-training-component',
  imports: [FormsModule],
  templateUrl: './athlete-training-component.html',
  styleUrl: './athlete-training-component.css',
})
export class AthleteTrainingComponent implements OnInit {

  currentUser: User | null = null;
  displayedTrainers: Trainer[] = [];
  allSports: Sport[] = [];
  allFacilities: SportsFacility[] = [];
  //search
  selectedSportId: number = 0;
  selectedFacilityId: number = 0;

  private trainerService = inject(TrainerService);
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

}
